package app.controller;

import app.dto.likes.LikeDto;
import app.dto.likes.LikeResponse;
import app.dto.main.ErrorResponse;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.service.PostLikeService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
@Api(tags = "Работа с \"Лайками\"")
public class LikesController {

    private final PostLikeService postLikeService;

    public LikesController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @GetMapping("/liked")
    @ApiOperation(value = "Был ли поставлен лайк пользователем", notes = "Был ли поставлен лайк пользователем")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = LikeResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<LikeResponse> isLiked(
            @ApiParam(value = "ID пользователя", defaultValue = "12")
            @RequestParam("user_id") Long userId,
            @ApiParam(value = "ID объекта у которого необходимо получить \"Лайки\"", defaultValue = "42")
            @RequestParam("item_id") Long itemId,
            @ApiParam(value = "Post, Comment", allowableValues = "Post,Comment")
            @RequestParam String type) throws BadRequestException {
        return postLikeService.isLiked(userId, itemId, type);
    }

    @GetMapping("/likes")
    @ApiOperation(value = "Получить список лайков", notes = "Получить список лайков")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Получить список пользователей оставивших лайк", response = LikeResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<LikeResponse> getLikesList(@ApiParam(value = "ID объекта у которого необходимо получить \"Лайки\"", defaultValue = "42")
                                                     @RequestParam("item_id") Long itemId,
                                                     @ApiParam(value = "Post, Comment", allowableValues = "Post,Comment")
                                                     @RequestParam String type) throws BadRequestException {
        return postLikeService.getLikesList(itemId, type);
    }

    @PutMapping("/likes")
    @ApiOperation(value = "Поставить лайк", notes = "Поставить лайк")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = LikeResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<LikeResponse> addLike(@RequestHeader("Authorization") String token,
                                                @RequestBody LikeDto likeDto) throws AppException {
        return postLikeService.addLike(token, likeDto);
    }

    @DeleteMapping("/likes")
    @ApiOperation(value = "Убрать лайк", notes = "Убрать лайк")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = LikeResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<LikeResponse> removeLike(@RequestHeader("Authorization") String token,
                                                   @ApiParam(value = "ID объекта у которого необходимо получить \"Лайки\"", defaultValue = "42")
                                                   @RequestParam("item_id") Long itemId,
                                                   @ApiParam(value = "Post, Comment", allowableValues = "Post,Comment")
                                                   @RequestParam String type) throws AppException {
        return postLikeService.removeLike(token, itemId, type);
    }
}
