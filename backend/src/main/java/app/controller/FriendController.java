package app.controller;

import app.dto.friend.FriendRequest;
import app.dto.friend.FriendStatusListResponse;
import app.dto.main.ErrorResponse;
import app.dto.main.MessageResponse;
import app.dto.profile.PersonListResponse;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import app.service.FriendService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
@Api(tags = "Работа с друзьями")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    @ApiOperation(value = "Получить список друзей", notes = "Получить список друзей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = PersonListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PersonListResponse> getFriends(
            @ApiParam(value = "Имя друга для поиска", defaultValue = "Петр")
            @RequestParam(required = false, defaultValue = "") String name,
            @ApiParam(value = "Отступ от начала списка", defaultValue = "0")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(required = false, defaultValue = "10") int itemPerPage,
            @RequestHeader("Authorization") String token
    ) throws UnAuthorizedException {

        return friendService.friendsAll(name, offset, itemPerPage, token);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удалить пользователя из друзей", notes = "Удалить пользователя из друзей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное удаление друга", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> deleteFriends(
            @ApiParam(value = "Id друга", defaultValue = "1")
            @PathVariable long id,
            @RequestHeader("Authorization") String token
    ) throws AppException {

        return friendService.friendDelete(id, token);
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "Принять/Добавить пользователя в друзья", notes = "Принять/Добавить пользователя в друзья")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> postFriends(
            @ApiParam(value = "Id пользователя", defaultValue = "1")
            @PathVariable long id,
            @RequestHeader("Authorization") String token
    ) throws AppException {
        return friendService.friendAdd(id, token);
    }

    @GetMapping("/request")
    @ApiOperation(value = "Получить список заявок", notes = "Получить список входящик заявок на добавление в друзья")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка заявок", response = PersonListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PersonListResponse> request(
            @ApiParam(value = "Имя пользователя для поиска по заявкам", defaultValue = "Петр")
            @RequestParam(required = false, defaultValue = "") String name,
            @ApiParam(value = "Отступ от начала списка", defaultValue = "0")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(required = false, defaultValue = "10") int itemPerPage,
            @RequestHeader("Authorization") String token
    ) throws UnAuthorizedException {
        return friendService.friendsRequest(name, offset, itemPerPage, token);
    }


    @GetMapping("/recommendations")
    @ApiOperation(value = "Получить список рекомендаций", notes = "Получить список рекомендаций")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка рекомендаций", response = PersonListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PersonListResponse> recommendations(
            @ApiParam(value = "Отступ от начала списка", defaultValue = "0")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "10")
            @RequestParam(required = false, defaultValue = "10") int itemPerPage,
            @RequestHeader("Authorization") String token
    ) throws UnAuthorizedException {
        return friendService.friendsRecommendations(offset, itemPerPage, token);
    }


    @PostMapping
    @ApiOperation(value = "Получить информацию является ли пользователь другом указанных пользователей", notes = "Получить информацию является ли пользователь другом указанных пользователей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = FriendStatusListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<FriendStatusListResponse> recommendations(
            FriendRequest request,
            @RequestHeader("Authorization") String token
    ) throws UnAuthorizedException {
        return friendService.friendAndFriend(request, token);
    }


}
