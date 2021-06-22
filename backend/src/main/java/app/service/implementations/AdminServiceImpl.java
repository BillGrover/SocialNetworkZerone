package app.service.implementations;

import app.config.AppConstant;
import app.dto.admin.*;
import app.dto.auth.AuthRequest;
import app.dto.main.MessageResponse;
import app.exceptions.*;
import app.model.Admin;
import app.model.Person;
import app.model.enums.*;
import app.repository.*;
import app.service.*;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    private final MainService mainService;
    private final PersonRepository personRepository;
    private final PostLikeRepository postLikeRepository;
    private final AdminRepository adminRepository;
    private final ProfileService profileService;
    private static final String DELETE_ADMIN_NAME = "Профиль удален";

    @Value("${jwt.token.secret}")
    private String secret;

    public AdminServiceImpl(MainService mainService,
                            PersonRepository personRepository,
                            PostLikeRepository postLikeRepository,
                            AdminRepository adminRepository,
                            ProfileService profileService) {
        this.mainService = mainService;
        this.personRepository = personRepository;
        this.postLikeRepository = postLikeRepository;
        this.adminRepository = adminRepository;
        this.profileService = profileService;
    }

    @Override
    public ResponseEntity<MessageResponse> setBlock(long id, String token) throws AppException {
        checkAdminByToken(token);
        Person person = mainService.getPersonById(id);
        if (person.getApproval() == ApprovalStatus.APPROVED) {
            person.setBlock(BlockStatus.BLOCKED);
            personRepository.save(person);
        }
        return ResponseEntity.ok(new MessageResponse("Доступ пользователя заблокирован"));
    }

    @Override
    public ResponseEntity<MessageResponse> setTotalBlock(long id, String token) throws AppException {
        checkAdminByToken(token);
        Person person = mainService.getPersonById(id);
        if (person.getApproval() == ApprovalStatus.APPROVED) {
            person = mainService.hidePersonInfo(person);
            person.setBlock(BlockStatus.TOTAL_BLOCKED);
            person.setAbout("Пользователь заблокирован " + new SimpleDateFormat(AppConstant.DATE_FORMAT).format(new Date()));
            person.setFirstName("Blocked");
            person.setLastName("User");
            person.setPhoto("/static/test/blocked.jpg");

            personRepository.save(person);
        }
        return ResponseEntity.ok(new MessageResponse("Вся информация пользователя заблокирована"));
    }

    @Override
    public ResponseEntity<MessageResponse> setUnBlock(long id, String token) throws AppException {
        checkAdminByToken(token);
        Person person = mainService.getPersonById(id);
        if (person.getApproval() == ApprovalStatus.APPROVED) {
            if (person.getBlock() == BlockStatus.TOTAL_BLOCKED) {
                person = mainService.restorePersonInfo(person);
            }
            person.setBlock(BlockStatus.UNBLOCKED);
            personRepository.save(person);
        }
        return ResponseEntity.ok(new MessageResponse("Пользователь разблокирован"));
    }

    @Override
    public ResponseEntity<MessageResponse> deletePerson(long id, String token) throws AppException {
        checkAdminByToken(token);
        Person person = mainService.getPersonById(id);
        profileService.deletePerson(person);
        return ResponseEntity.ok(new MessageResponse("Пользователь удален"));
    }

    @Override
    public ResponseEntity<MessageResponse> restorePerson(long id, String token) throws AppException {
        checkAdminByToken(token);
        Person person = mainService.getPersonById(id);
        profileService.restorePerson(person);
        return ResponseEntity.ok(new MessageResponse("Пользователь восстановлен"));
    }

    @Override
    public ResponseEntity<AllStatisticsResponse> getAllStatistics(String token) throws UnAuthorizedException {
        checkAdminByToken(token);
        AllStatisticsResponse allStatisticsResponse = mainService.getPersonsCommentsPostsCount();
        allStatisticsResponse.setLikes(postLikeRepository.count());
        return ResponseEntity.ok(allStatisticsResponse);
    }

    @Override
    public ResponseEntity<List<PersonAdminPanelDto>> getPersonList(String token) throws UnAuthorizedException {
        checkAdminByToken(token);
        String email = getEmailAdmin(token);
        Admin admin = getAdminByEmail(email);
        List<PersonAdminPanelDto> personAdminPanelDtoList = new ArrayList<>();
        String status = admin.getStatus().toString();
        if (status.equals(AdminRole.ADMIN.toString())) {
            Iterable<Person> personIterable = personRepository.findAll();
            personIterable.forEach(person -> {
                try {
                    personAdminPanelDtoList.add(new PersonAdminPanelDto(person, mainService));
                } catch (BadRequestException e) {
                    e.printStackTrace();
                }
            });
        }
        if (status.equals(AdminRole.MODERATOR.toString())) {
            List<Person> personList = personRepository
                    .findAllByApproval(ApprovalStatus.APPROVED);
            personList.forEach(person -> {
                try {
                    personAdminPanelDtoList.add(new PersonAdminPanelDto(person, mainService));
                } catch (BadRequestException e) {
                    e.printStackTrace();
                }
            });
        }
        return ResponseEntity.ok(personAdminPanelDtoList);
    }

    @Override
    public ResponseEntity<List<AdminDto>> getAdminList(String token) throws UnAuthorizedException {
        checkAdminByToken(token);
        Iterable<Admin> adminIterable = adminRepository.findAll();
        List<AdminDto> adminDtoList = new ArrayList<>();
        adminIterable.forEach(admin -> {
            if (!admin.getName().equals(DELETE_ADMIN_NAME) && !admin.getEmail().equals(getEmailAdmin(token))) {
                adminDtoList.add(new AdminDto(admin));
            }
        });
        return ResponseEntity.ok(adminDtoList);
    }

    @Override
    public ResponseEntity<List<ApplicantForAdmin>> getApplicantList(String token) throws UnAuthorizedException {
        checkAdminByToken(token);
        List<Person> personList = personRepository.findForApplicants();
        List<ApplicantForAdmin> applicantForAdminList = new ArrayList<>();
        List<String> emailAdminList = new ArrayList<>();
        Iterable<Admin> adminIterator = adminRepository.findAll();

        adminIterator.forEach(admin -> emailAdminList.add(admin.getEmail()));
        personList.forEach(person -> {
                    if (!emailAdminList.contains(person.getEmail())) {
                        applicantForAdminList.add(new ApplicantForAdmin(person));
                    } else {
                        Admin admin = null;
                        try {
                            admin = getAdminByEmail(person.getEmail());
                        } catch (UnAuthorizedException e) {
                            e.printStackTrace();
                        }
                        assert admin != null;
                        if (admin.getName().equals(DELETE_ADMIN_NAME)) {
                            applicantForAdminList.add(new ApplicantForAdmin(person));
                        }

                    }
                }
        );
        return ResponseEntity.ok(applicantForAdminList);
    }

    @Override
    public ResponseEntity<MessageResponse> addAdmin(String stringRole, long id, String token) throws AppException {
        checkAdminByToken(token);
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Данного пользователя добавить нельзя"));
        AdminRole role = AdminRole.valueOf(stringRole.toUpperCase());
        Admin admin;
        if (adminRepository.findByEmail(person.getEmail()).isPresent()) {
            admin = getAdminByEmail(person.getEmail());
            admin.setName(String.format("%s %s", person.getFirstName(), person.getLastName()));
            admin.setImageUrl(person.getPhoto());
            admin.setPassword(person.getPassword());
            admin.setStatus(role);
        } else {
            admin = new Admin(person, role);
        }
        adminRepository.save(admin);

        String message = String.format("%s добавлен",
                admin.getStatus() == AdminRole.ADMIN ? "Администратор" : "Модератор");
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @Override
    public ResponseEntity<MessageResponse> updateAdmin(long id, String role, String token) throws AppException {
        checkAdminByToken(token);
        Admin admin = findAdminById(id);
        admin.setStatus(AdminRole.valueOf(role.toUpperCase()));
        adminRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse("Данные обновлены"));
    }

    @Override
    public ResponseEntity<MessageResponse> removeAdmin(long id, String token) throws AppException {
        checkAdminByToken(token);
        Admin admin = findAdminById(id);
        String message = String.format("%s удален",
                admin.getStatus() == AdminRole.ADMIN ? "Администратор" : "Модератор");
        admin.setName(DELETE_ADMIN_NAME);
        admin.setPassword("-");
        admin.setImageUrl("/static/test/admin-def.png");
        adminRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @Override
    public ResponseEntity<AdminLoginResponse> adminLogin(AuthRequest authRequest) throws AppException {
        Admin admin = getAdminByEmail(authRequest.getEmail());
        if (!new BCryptPasswordEncoder().matches(authRequest.getPassword(), admin.getPassword())) {
            throw new BadRequestException("Неверный пароль");
        }
        String token = createAdminToken(admin);
        AdminLoginResponse adminLoginResponse = new AdminLoginResponse(admin, token);
        return ResponseEntity.ok(adminLoginResponse);
    }

    @Override
    public ResponseEntity<AdminLoginResponse> adminLoginByToken(String token) throws AppException {
        String email = getEmailAdmin(token);
        Admin admin = getAdminByEmail(email);
        return ResponseEntity.ok(new AdminLoginResponse(admin, token));
    }

    @Override
    public Admin getAdminByEmail(String email) throws UnAuthorizedException {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new UnAuthorizedException(String.format("Администратор %s не найден", email)));
    }

    private String createAdminToken(Admin admin) {
        Claims claims = Jwts.claims().setSubject(admin.getEmail());
        claims.put("name", admin.getName());
        claims.put("role", admin.getStatus().toString());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private String getEmailAdmin(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public Admin checkAdminByToken(String token) throws UnAuthorizedException {
        String email = getEmailAdmin(token);
        if (email == null) {
            throw new UnAuthorizedException("Требуется авторизация");
        }
        return getAdminByEmail(email);
    }

    @Override
    public Admin findAdminById(long id) throws BadRequestException {
        return adminRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Администратор id:%s не найден", id)));
    }

}
