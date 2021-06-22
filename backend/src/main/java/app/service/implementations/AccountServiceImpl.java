package app.service.implementations;

import app.dto.account.*;
import app.dto.main.MessageResponse;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.model.*;
import app.model.enums.ApprovalStatus;
import app.model.enums.NotificationType;
import app.repository.*;
import app.service.*;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.*;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    private final PersonRepository personRepository;
    private final ResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final MainService mainService;

    public AccountServiceImpl(
            PersonRepository personRepository,
            ResetTokenRepository tokenRepository,
            EmailService emailService,
            BCryptPasswordEncoder passwordEncoder,
            AuthService authService,
            NotificationSettingsRepository notificationSettingsRepository,
            MainService mainService) {
        this.personRepository = personRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.notificationSettingsRepository = notificationSettingsRepository;
        this.mainService = mainService;
    }

    @Value("${limit.approval.expirationPerSec}")
    private long approvalPeriodPerSec;

    @Value("${limit.reset.expirationPerSec}")
    private long resetTokenPeriodPerSec;

    @Override
    public ResponseEntity<MessageResponse> addNewUser(
            RegistrationRequest request,
            HttpServletRequest httpServletRequest) throws BadRequestException {

        if (request.getPassword().equals(request.getPasswdConfirm())) {
            Optional<Person> personOptional = personRepository.findByEmail(request.getEmail());
            if (personOptional.isEmpty() || personOptional.get().getApproval().equals(ApprovalStatus.DELETED)) {

                Person newPerson = Person.createNotApprovedPerson(request);
                if (personOptional.isPresent()) {
                    personOptional.get().setEmail(null);
                    personRepository.save(personOptional.get());
                }
                personRepository.save(newPerson);

                Claims claims = Jwts.claims().setSubject(request.getEmail());

                String token = Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS256, "secret")
                        .compact();

                String link = emailService.getHostAndPort(httpServletRequest) +
                        "/api/v1/account/approve?code="
                        + newPerson.getConfirmationCode()
                        + "&token=" + token;
                emailService.sendRegistrationEmail(
                        request.getEmail(), link);

            } else {
                throw new BadRequestException("Такой Email уже зарегистрирован");
            }
        } else {
            throw new BadRequestException("Пароли не совадают");
        }

        return ResponseEntity.ok(MessageResponse.ok());
    }

    public String isApprove(
            String code,
            String token,
            HttpServletRequest request) throws BadRequestException {

        String email = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody().getSubject();
        Date limitDate = new Date(System.currentTimeMillis() - approvalPeriodPerSec * 1000);
        personRepository.deleteOldRegistrationRequest(limitDate);
        Person user = mainService.getPersonByEmail(email);
        if (user.getConfirmationCode().equals(code)) {
            if (user.getApproval() == ApprovalStatus.REJECTED) {
                String link = emailService.getHostAndPort(request) + "/";
                emailService.sendSuccessEmail(email, link);
                user.approvalPassed();
                personRepository.save(user);
            }
            return "index";
        } else {
            throw new BadRequestException("Bad confirm code");
        }
    }

    /**
     * Метод для восстановления пароля
     */
    @Override
    public ResponseEntity<MessageResponse> recoveryPassword(
            String email, HttpServletRequest request, HttpServletResponse response)
            throws BadRequestException {
        Person person = mainService.getPersonByEmail(email);
        String token = mainService.createToken(person);
        String link = createPasswordForgotLink(request, person);
        emailService.sendRecoveryPasswordEmail(email, link);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        MessageResponse meResponse = new MessageResponse("ok");
        return ResponseEntity.ok(meResponse);
    }

    @Override
    public ResponseEntity<MessageResponse> setPassword(String password, String token,
                                                       HttpServletRequest request)
            throws AppException {
        if (token == null) {
            Optional<String> optionalToken = mainService.getCookie(request, "jwt");
            if (optionalToken.isPresent()) {
                token = optionalToken.get();
            }
        }
        Person person = mainService.getPersonByToken(token);
        String updatedPassword = passwordEncoder.encode(password);
        person.setPassword(updatedPassword);
        personRepository.save(person);
        MessageResponse response = new MessageResponse("ok");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponse> setEmail(String newEmail, String token,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response)
            throws AppException {
        Person person = mainService.getPersonByToken(token);
        if (personRepository.existsByEmail(newEmail)) {
            throw new BadRequestException("Пользователь с таким email уже существует");
        }
        person.setEmail(newEmail);
        personRepository.save(person);
        MessageResponse newResponse = new MessageResponse("ok");
        String link = emailService.getHostAndPort(request) + "/";
        emailService.sendSuccessResetEmail(newEmail, link);
        authService.authLogout(request, response);
        return ResponseEntity.ok(newResponse);
    }

    /**
     * Метод для изменения текущего пароля
     */
    @Override
    public ResponseEntity<MessageResponse> changePassword(String token, HttpServletRequest request)
            throws AppException {
        Person person = mainService.getPersonByToken(token);
        String link = createPasswordResetLink(request, person);
        emailService.sendChangePasswordEmail(person.getEmail(), link);
        return ResponseEntity.ok(MessageResponse.ok());
    }

    /**
     * Метод для изменения текущего адреса электроной почты
     */
    @Override
    public ResponseEntity<MessageResponse> changeEmail(String token, HttpServletRequest request)
            throws AppException {
        Person person = mainService.getPersonByToken(token);
        String link = createEmailResetLink(request, person);
        emailService.sendChangeEmailEmail(person.getEmail(), link);
        return ResponseEntity.ok(MessageResponse.ok());
    }

    @Override
    public ResponseEntity<NotificationListResponse> getNotification(String token) throws AppException {
        //получаем текущего пользователя
        Person person = mainService.getPersonByToken(token);

        NotificationListResponse notificationListResponse = new NotificationListResponse();
        List<NotificationsDto> notificationsDtoList = new ArrayList<>();
        List<String> listTypeTrue = new ArrayList<>();
        List<NotificationsSettings> notificationsSettingsList = notificationSettingsRepository.findAllByPersons(person);
        for (NotificationsSettings notificationsSettings : notificationsSettingsList) {
            NotificationsDto notificationsDto = new NotificationsDto();
            notificationsDto.setEnable(true);
            notificationsDto.setType(notificationsSettings.getType());
            //добавляем настройки что включены
            listTypeTrue.add(notificationsSettings.getType());
            notificationsDtoList.add(notificationsDto);
        }
        //добавляем того чего нет в базе
        for (NotificationType type : NotificationType.values()) {
            if (!listTypeTrue.contains(type.toString())) {
                NotificationsDto notificationsDto = new NotificationsDto();
                notificationsDto.setEnable(false);
                notificationsDto.setType(type.toString());
                notificationsDtoList.add(notificationsDto);
            }
        }
        notificationListResponse.setNotifications(notificationsDtoList);

        return ResponseEntity.ok(notificationListResponse);
    }

    @Override
    public ResponseEntity<MessageResponse> changeNotification(NotificationAccountDto request, String token)
            throws AppException {
        //получаем текущего пользователя
        Person person = mainService.getPersonByToken(token);

        if (request.isEnable()) {
            NotificationsSettings notificationsSettings = new NotificationsSettings();
            notificationsSettings.setPersons(person);
            notificationsSettings.setType(request.getType().toString());
            notificationSettingsRepository.save(notificationsSettings);
        } else {
            notificationSettingsRepository.deleteByPersonsAndType(person, request.getType().toString());
        }

        return ResponseEntity.ok(new MessageResponse("ok"));
    }



    private String createResetLink(HttpServletRequest request, Person person, String target) {
        ResetToken resetToken = new ResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setPerson(person);
        resetToken.setExpiryDate(resetTokenPeriodPerSec);
        tokenRepository.deleteAllByPersonIdOrExpiryDateBefore(person.getId(), new Date());
        tokenRepository.save(resetToken);
        return emailService.getHostAndPort(request) +
                "/api/v1/account/reset-" + target + "?token=" + resetToken.getToken();
    }

    private String createForgotLink(HttpServletRequest request, Person person) {
        ResetToken resetToken = new ResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setPerson(person);
        resetToken.setExpiryDate(resetTokenPeriodPerSec);
        tokenRepository.deleteAllByPersonIdOrExpiryDateBefore(person.getId(), new Date());
        tokenRepository.save(resetToken);
        return emailService.getHostAndPort(request) +
                "/api/v1/account/forgot-password?token=" + resetToken.getToken();
    }

    private String createPasswordResetLink(HttpServletRequest request, Person person) {
        return createResetLink(request, person, "password");
    }

    private String createEmailResetLink(HttpServletRequest request, Person person) {
        return createResetLink(request, person, "email");
    }

    private String createPasswordForgotLink(HttpServletRequest request, Person person) {
        return createForgotLink(request, person);
    }

}
