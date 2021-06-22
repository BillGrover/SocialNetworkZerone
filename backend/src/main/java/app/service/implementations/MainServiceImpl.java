package app.service.implementations;

import app.dto.admin.*;
import app.exceptions.BadRequestException;
import app.exceptions.UnAuthorizedException;
import app.model.*;
import app.model.enums.MessagePermission;
import app.model.enums.NotificationType;
import app.repository.*;
import app.security.jwt.JwtTokenProvider;
import app.service.LocationService;
import app.service.MainService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MainServiceImpl implements MainService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final LocationService locationService;

    public MainServiceImpl(JwtTokenProvider jwtTokenProvider,
                           PersonRepository personRepository,
                           PostRepository postRepository,
                           PostCommentRepository postCommentRepository,
                           NotificationRepository notificationRepository,
                           NotificationSettingsRepository settingsRepositoryRepository,
                           LocationService locationService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.notificationRepository = notificationRepository;
        this.notificationSettingsRepository = settingsRepositoryRepository;
        this.locationService = locationService;
    }

    @Override
    public String convertToSqlLikeFormat(String string) {
        return (string != null) ? "%" + string + "%" : null;
    }

    @Override
    public String createToken(Person person) {
        return jwtTokenProvider.createToken(person.getEmail(), person.getFirstName(), person.getLastName());
    }

    @Override
    public Person getPersonByToken(String token) throws UnAuthorizedException {
        String email = jwtTokenProvider.getEmail(token)
                .orElseThrow(() -> new UnAuthorizedException("Требуется авторизация"));
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new UnAuthorizedException("Требуется регистрация"));
    }

    @Override
    public Person getPersonById(long id) throws BadRequestException {
        return personRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Пользователь id:%d не найден", id)));
    }

    @Override
    public Person getPersonByEmail(String email) throws BadRequestException {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException(String.format("Пользователь %s не найден", email)));
    }

    public Post getPostById(long id) throws BadRequestException {
        return postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Пост id:%d не найден", id)));
    }

    @Override
    public PostComment getPostCommentById(long id) throws BadRequestException {
        return postCommentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Комментарий id:%d не найден", id)));
    }

    @Override
    public Optional<String> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public void addNotification(Person entityPerson, Person author, NotificationType type) {
        //смотрим настройки оповещений кому отправляем его
        int count = notificationSettingsRepository.countByPersonsAndType(entityPerson, type.toString());
        //если оповещения разрешены
        //нельзя себе отправлять уведомления
        if (count > 0 && entityPerson.getId() != author.getId()) {
            //создаем оповещение
            Notification notification = new Notification();
            notification.setTime(new Date());
            notification.setNotificationType(type);
            notification.setReadStatus("SENT");
            notification.setAuthorPerson(author);
            notification.setTargetPerson(entityPerson);
            notificationRepository.save(notification);
        }
    }

    @Override
    public Person hidePersonInfo(Person person) {

        HashMap<String, String> archive = new HashMap<>();
        archive.put("email", person.getEmail());
        archive.put("firstName", person.getFirstName());
        archive.put("lastName", person.getLastName());
        archive.put("phone", person.getPhone());
        archive.put("photo", person.getPhoto());
        archive.put("birthday",
                (person.getBirthDate() != null)
                        ? String.valueOf(person.getBirthDate().getTime())
                        : null);
        archive.put("countyId",
                (person.getCountry() != null)
                        ? String.valueOf(person.getCountry().getId())
                        : null);
        archive.put("cityId",
                (person.getCity() != null)
                        ? String.valueOf(person.getCity().getId())
                        : null);
        archive.put("about", person.getAbout());

        person.setAbout(null);
        person.setBirthDate(null);
        person.setConfirmationCode(new JSONObject(archive).toJSONString());
        person.setFirstName(null);
        person.setLastName(null);
        person.setMessagePermission(null);
        person.setPhone("-");
        person.setPhoto(null);
        person.setCity(null);
        person.setCountry(null);

        return person;
    }

    @Override
    public Person restorePersonInfo(Person person) throws BadRequestException {
        JSONObject restore;
        String data = person.getConfirmationCode();
        if (data == null) throw new BadRequestException("Нет данных для восстановления");
        try {
            restore = (JSONObject) new JSONParser().parse(data);
        } catch (ParseException ignore) {
            throw new BadRequestException("Нет данных для восстановления");
        }

        person.setAbout((String) restore.get("about"));
        person.setFirstName((String) restore.get("firstName"));
        person.setLastName((String) restore.get("lastName"));
        person.setEmail((String) restore.get("email"));
        person.setPhone((String) restore.get("phone"));
        person.setPhoto((String) restore.get("photo"));

        Object restoreBirthDate = restore.get("birthday");
        if (restoreBirthDate != null) {
            person.setBirthDate(new Date(Long.parseLong((String) restoreBirthDate)));
        }

        Object restoreCountryId = restore.get("countyId");
        if (restoreCountryId != null) {
            person.setCountry(locationService.getCountyById(Long.parseLong((String) restoreCountryId)));
        }
        Object restoreCityId = restore.get("cityId");
        if (restoreCityId != null) {
            person.setCity(locationService.getCityById(Long.parseLong((String) restoreCityId)));
        }

        person.setMessagePermission(MessagePermission.ALL);
        person.setConfirmationCode(null);

        return person;
    }

    @Override
    public ResponseEntity<PersonListForAdmin> getPersonList() {
        List<Person> personList = (List<Person>) personRepository.findAll();
        List<PersonForAdminDto> list = personList.stream().map(PersonForAdminDto::new).collect(Collectors.toList());
        PersonListForAdmin response = new PersonListForAdmin(list);
        return ResponseEntity.ok(response);
    }

    @Override
    public AllStatisticsResponse getPersonsCommentsPostsCount() {
        AllStatisticsResponse allStatisticsResponse = new AllStatisticsResponse();
        allStatisticsResponse.setPersons(personRepository.count());
        allStatisticsResponse.setPosts(postRepository.count());
        allStatisticsResponse.setComments(postCommentRepository.count());
        return allStatisticsResponse;
    }

}