package app.controller;

import app.dto.main.*;
import app.dto.post.*;
import app.dto.profile.*;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.service.ProfileService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "Работа с публичной информацией пользователя")

public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    @ApiOperation(
            value = "Получить текущего пользователя",
            notes = "Успешное получение текущего пользователя")
    @ApiResponses({@ApiResponse(code = 200, message = " Успешное получение текущего пользователя", response = PersonResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class)})
    public ResponseEntity<PersonResponse> getPerson(
            @RequestHeader("Authorization") String token)
            throws AppException {
        return profileService.getPerson(token);
    }

    @PutMapping("/me")
    @ApiOperation(value = "Редактирование текущего пользователя", notes = "Редактирование пользователя")
    @ApiResponses({@ApiResponse(code = 200, message = "Успешное получение текущего пользователя", response = PersonResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PersonResponse> updatePerson(
            @RequestHeader("Authorization") String token,
            @RequestBody PersonUpdateRequest update)
            throws AppException {
        return profileService.updatePerson(token, update);
    }

    @DeleteMapping("/me")
    @ApiOperation(value = "Удаление текущего пользователя", notes = "Удаление текущего пользователя")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Успешное удаление текущего пользователя", response = MessageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public String deletePerson(
            @RequestHeader("Authorization") String token)
            throws AppException {
        return profileService.deletePerson(token);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получить пользователя по id", notes = "Получить пользователя по id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение пользователя", response = PersonResponse.class),//ответы на API
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PersonResponse> getPersonById(
            @ApiParam(value = "ID пользователя", defaultValue = "1")
            @PathVariable long id
    ) throws BadRequestException {
        return profileService.getPersonById(id);
    }

    @GetMapping("/{id}/wall")
    @ApiOperation(value = "Получение записей на стене пользователя", notes = "Получение записей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение публикаций", response = PostListResponse.class),//ответы на API
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PostListResponse> getUserPosts(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "ID пользователя", defaultValue = "1")
            @PathVariable long id,
            @ApiParam(value = "Отступ от начала списка")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(required = false, defaultValue = "${default.perPage}") int itemPerPage
    ) throws AppException {
        return profileService.getPostList(id, offset, itemPerPage, token);
    }

    /**
     * Controller method for creating posts.
     *
     * @param id          Author id.
     * @param publishDate Date of publication.
     * @param request     Request body.
     * @return Response to the format JSON.
     */
    @PostMapping("/{id}/wall")
    @ApiOperation(value = "Добавление публикации на стену пользователя", notes = "Добавление публикации")
    @ApiResponses({
            @ApiResponse(code = 200, message = " Успешное добавление публикации", response = PostResponse.class),//ответы на API
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PostResponse> addPost(
            @ApiParam(value = "ID пользователя", defaultValue = "1")
            @PathVariable long id,
            @ApiParam(value = "Отложить до определенной даты", defaultValue = "1559751301818")
            @RequestParam(name = "publish_date", defaultValue = "0") long publishDate,
            @RequestBody AddPostRequest request,
            @ApiParam(hidden = true) @RequestHeader("Authorization") String token
    ) throws AppException {
        return profileService.addPost(id, publishDate, request, token);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Поиск пользователя", notes = "Поиск")
    @ApiResponses({
            @ApiResponse(code = 200, message = " Успешное получение списка пользователей", response = PersonListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PersonListResponse> getSearch(
            @ApiParam(value = "Имя пользователя", defaultValue = "Григорий")
            @RequestParam(name = "first_name", required = false) String firstNameLike,
            @ApiParam(value = "Фамилия пользователя", defaultValue = "Пепс")
            @RequestParam(name = "last_name", required = false) String lastNameLike,
            @ApiParam(value = "Страна", defaultValue = "Россия")
            @RequestParam(name = "country", required = false) String countryName,
            @ApiParam(value = "Город", defaultValue = "Москва")
            @RequestParam(name = "city", required = false) String cityName,
            @ApiParam(value = "Кол-во лет ОТ", defaultValue = "18")
            @RequestParam(name = "age_from", required = false, defaultValue = "0") int ageFrom,
            @ApiParam(value = "Кол-во лет ДО", defaultValue = "50")
            @RequestParam(name = "age_to", required = false, defaultValue = "0") int ageTo,
            @ApiParam(value = "Отступ от начала списк")
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(name = "itemPerPage", defaultValue = "${default.perPage}") int perPage
    ) {
        return profileService.getSearch(
                firstNameLike,
                lastNameLike,
                cityName,
                countryName,
                ageFrom,
                ageTo,
                new PaginationSettings(offset, perPage));
    }

}