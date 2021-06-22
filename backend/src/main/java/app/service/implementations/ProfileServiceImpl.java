package app.service.implementations;

import app.config.AppConstant;
import app.dto.comment.CommentData;
import app.dto.main.MessageResponse;
import app.dto.main.PaginationSettings;
import app.dto.post.*;
import app.dto.profile.*;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.model.*;
import app.model.enums.*;
import app.repository.*;
import app.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${limit.age.min}")
    private int limitAge;

    private final PersonRepository personRepository;
    private final LocationService locationService;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final MainService mainService;
    private final PostService postService;
    private final TagService tagService;

    public ProfileServiceImpl(PersonRepository personRepository,
                              LocationService locationService, PostRepository postRepository,
                              PostCommentRepository postCommentRepository,
                              MainService mainService,
                              PostService postService, TagService tagService) {
        this.personRepository = personRepository;
        this.locationService = locationService;
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.mainService = mainService;
        this.postService = postService;
        this.tagService = tagService;
    }

    @Override
    public ResponseEntity<PersonResponse> getPerson(String token) throws AppException {
        Person person = mainService.getPersonByToken(token);
        PersonResponse response = new PersonResponse();
        response.setData(new PersonDto(person));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PersonResponse> getPersonById(long id) throws BadRequestException {
        Person person = mainService.getPersonById(id);
        PersonResponse response = new PersonResponse();
        response.setData(new PersonDto(person));
        return ResponseEntity.ok(response);
    }

    @Override
    public String deletePerson(String token) throws AppException {
        Person person = mainService.getPersonByToken(token);
        deletePerson(person);
        return "logout";
    }

    @Override
    public void deletePerson(Person person) {
        person = mainService.hidePersonInfo(person);
        person.setApproval(ApprovalStatus.DELETED);
        person.setAbout("Пользователь удален " + new SimpleDateFormat(AppConstant.DATE_FORMAT).format(new Date()));
        person.setFirstName("No");
        person.setLastName("Name");
        person.setBlock(BlockStatus.BLOCKED);
        person.setPhoto("/static/img/deleted_avatar.png");
        personRepository.save(person);
    }

    public void restorePerson(Person person) throws BadRequestException {
        person = mainService.restorePersonInfo(person);
        person.approvalPassed();
        personRepository.save(person);
    }

    @Override
    public ResponseEntity<PersonResponse> updatePerson(String token, PersonUpdateRequest update) throws AppException {
        Person person = mainService.getPersonByToken(token);
        updatePerson(person, update);
        personRepository.save(person);
        PersonResponse response = new PersonResponse();
        response.setData(new PersonDto(person));
        return ResponseEntity.ok(response);
    }

    private void updatePerson(Person person, PersonUpdateRequest update) throws BadRequestException {
        person.setFirstName(update.getFirstName());
        person.setLastName(update.getLastName());
        Date limitDate = Date.from(LocalDateTime.now().minusYears(limitAge).atZone(ZoneId.systemDefault()).toInstant());
        if (update.getBirthDate().before(limitDate)) {
            person.setBirthDate(update.getBirthDate());
        } else throw new BadRequestException("Ваш возраст менее " + limitAge + " лет");
        person.setPhone(update.getPhone());
        person.setAbout(update.getAbout());
        long countyId = update.getCountryId();
        long cityId = update.getCityId();
        Optional<Country> optionalCountry = locationService.findCountyById(countyId);
        optionalCountry.ifPresent(person::setCountry);
        Optional<City> optionalCity = locationService.findCityById(cityId);
        optionalCity.ifPresent(person::setCity);
    }

    @Override
    public ResponseEntity<PostResponse> addPost(
            long authorId,
            long publishDate,
            AddPostRequest request,
            String token)
            throws AppException {

        Person authPerson = mainService.getPersonByToken(token);
        Post post = createPost(authorId, publishDate, request);
        List<Person> listFriends = personRepository.findByFriendsAll(authorId);
        return getPostResponseAndAddNotification(authPerson, post, listFriends);
    }

    @Override
    public ResponseEntity<PostResponse> updatePost(
            long postId,
            long publishDate,
            AddPostRequest request,
            String token)
            throws AppException {

        Person authPerson = mainService.getPersonByToken(token);

        Post post = updateTekPost(postId, publishDate, request);
        List<Person> listFriends = personRepository.findByFriendsAll(post.getAuthor().getId());
        return getPostResponseAndAddNotification(authPerson, post, listFriends);
    }

    @Override
    public ResponseEntity<MessageResponse> deletePost(long postId) throws BadRequestException {
        try {
            postRepository.deleteById(postId);
            return ResponseEntity.ok(new MessageResponse("Пост id:" + postId + " удален"));
        } catch (Exception e) {
            throw new BadRequestException("Пост не удален");
        }
    }

    @Override
    public ResponseEntity<PostListResponse> getPostList(long id, int offset, int itemPerPage, String token)
            throws AppException {

        postService.deleteComments();//Проверка удалленных комментариев для окончательного удаления из базы

        PostListResponse response = new PostListResponse();
        Person person = mainService.getPersonById(id);
        List<Post> postList = postRepository.findByAuthorOrderByTimeDesc(person);
        response.setTotal(postList.size());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        List<PostResponseData> dataList = response.getData();
        Person authPerson = mainService.getPersonByToken(token);
        postList.forEach(post -> dataList.add(new PostResponseData(post, authPerson)));
        return ResponseEntity.ok(response);
    }

    /**
     * Поиск пользователя по параметрам
     *
     * @param firstName   имя(часть имени), необязательный параметр
     * @param lastName    фамилия(часть фамилии), необязательный параметр
     * @param cityName    полное нзвание города, необязательный параметр
     * @param countryName полное название страны, необязательный параметр
     * @param ageFrom     возраст начала диапазона поиска, по-умолчанию 0
     * @param ageTo       возраст конца диапазона поиска, по-умолчанию 0
     * @return {@link List список} {@link Person пользователей}
     */
    @Override
    public ResponseEntity<PersonListResponse> getSearch(
            String firstName,
            String lastName,
            String cityName,
            String countryName,
            int ageFrom,
            int ageTo,
            PaginationSettings settings) {


        boolean strict = !(lastName == null && cityName == null && countryName == null && ageFrom == 0 && ageTo == 0);

        //вычисление даты
        Date dateFrom = null;
        Date dateTo = null;
        if (ageFrom != 0 || ageTo != 0) {
            LocalDateTime from = (ageTo == 0) ? LocalDateTime.now().minusYears(100) : LocalDateTime.now().minusYears(ageTo);
            LocalDateTime to = LocalDateTime.now().minusYears(ageFrom);
            dateFrom = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
            dateTo = Date.from(to.atZone(ZoneId.systemDefault()).toInstant());
        }

        List<Person> personList = (!strict)
                ? personRepository.search(mainService.convertToSqlLikeFormat(firstName))
                : personRepository.strictSearch(
                mainService.convertToSqlLikeFormat(firstName),
                mainService.convertToSqlLikeFormat(lastName),
                countryName,
                cityName,
                dateFrom, dateTo
        );

        PersonListResponse response = new PersonListResponse();
        response.setData(
                personList
                        .stream()
                        .map(PersonDto::new)
                        .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }


    private Post createPost(long authorId,
                            long publishDate,
                            AddPostRequest request) throws BadRequestException {
        Person person = mainService.getPersonById(authorId);
        Post post = new Post();
        post.setAuthor(person);
        return updatePostWithTagsAndSave(post, publishDate, request);
    }

    private Post updateTekPost(long postId,
                               long publishDate,
                               AddPostRequest request) throws BadRequestException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException("Невозможно обновить"));
        return updatePostWithTagsAndSave(post, publishDate, request);
    }

    private Post updatePostWithTagsAndSave(Post post, long publishDate, AddPostRequest request) {
        if (publishDate == 0 || new Date(publishDate).before(new Date())) {
            post.setTime(new Date());
        } else {
            post.setTime(new Date(publishDate));
        }
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        postRepository.save(post);
        tagService.addTags2Posts(post, request.getTags());
        return post;
    }


    private void addComments(PostResponseData data, List<PostComment> postComments, Person authUser) {
        List<CommentData> comments = new ArrayList<>();
        postComments.forEach(postComment -> comments.add(new CommentData(postComment, authUser)));
        data.setComments(comments);
    }


    private ResponseEntity<PostResponse> getPostResponseAndAddNotification(Person authPerson, Post post, List<Person> listFriends)  {
        for (Person friend : listFriends) {
            //оповещение
            mainService.addNotification(friend, authPerson, NotificationType.POST);
        }
        PostResponseData data = new PostResponseData(post, authPerson);
        List<PostComment> postComments = postCommentRepository.findPostCommentsByPost(post);
        addComments(data, postComments, authPerson);
        PostResponse response = new PostResponse(data);

        return ResponseEntity.ok(response);
    }

}


