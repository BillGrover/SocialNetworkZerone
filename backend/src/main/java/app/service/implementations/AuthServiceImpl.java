package app.service.implementations;

import app.dto.auth.AuthRequest;
import app.dto.auth.AuthResponse;
import app.dto.profile.PersonDto;
import app.exceptions.BadRequestException;
import app.exceptions.UnAuthorizedException;
import app.model.Person;
import app.model.enums.ApprovalStatus;
import app.model.enums.BlockStatus;
import app.repository.PersonRepository;
import app.security.jwt.JwtTokenProvider;
import app.service.AuthService;
import app.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.*;
import java.util.Date;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonRepository personRepository;
    private final MainService mainService;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider, PersonRepository personRepository, MainService mainService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.personRepository = personRepository;
        this.mainService = mainService;
    }

    @Override
    public ResponseEntity<AuthResponse> authLogin(AuthRequest request, HttpServletResponse httpServletResponse) throws BadRequestException {
        AuthResponse response = new AuthResponse();
        Person person = mainService.getPersonByEmail(request.getEmail());
        if (person.getApproval().equals(ApprovalStatus.DELETED)) {
            throw new BadRequestException("Пользователь не найден");
        } else if (person.getApproval() != ApprovalStatus.APPROVED) {
            throw new BadRequestException("Использованы неподтвержденные данные");
        } else if (person.getBlock() == BlockStatus.BLOCKED || person.getBlock() == BlockStatus.TOTAL_BLOCKED) {
            throw new BadRequestException("Пользователь заблокирован, обратитесь к администратору");
        } else if (!new BCryptPasswordEncoder().matches(request.getPassword(), person.getPassword())) {
            throw new BadRequestException("Неверный пароль");
        } else {
            String token = jwtTokenProvider
                    .createToken(
                            request.getEmail(),
                            person.getFirstName(),
                            person.getLastName()
                    );

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);

            personRepository.save(person.online());

            response.setTimestamp(new Date().getTime());
            response.setData(new PersonDto(person, token));

        }
        return ResponseEntity.ok(response);
    }

    @Override
    public void authLogout(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> optionalToken = mainService.getCookie(request, "jwt");
        if (optionalToken.isPresent()) {
            String token = optionalToken.get();
            try {
                Person person = mainService.getPersonByToken(token);
                personRepository.save(person.offline());
            } catch (UnAuthorizedException ignored) {
                // игнорируем ошибку при logout
            }
            response.setHeader("Authorization", null);
            Cookie cookie = new Cookie("jwt", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

}

