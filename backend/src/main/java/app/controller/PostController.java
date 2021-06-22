package app.controller;

import app.dto.comment.*;
import app.dto.main.*;
import app.dto.post.*;
import app.exceptions.*;
import app.service.*;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
@Api(tags = "Работа с публикациями")
public class PostController {

    private final CommentService commentService;
    public final PostService postService;
    private final ProfileService profileService;

    public PostController(CommentService commentService, PostService postService, ProfileService profileService) {
        this.commentService = commentService;
        this.postService = postService;
        this.profileService = profileService;
    }

    @GetMapping("/post/{id}/comments")
    @ApiOperation(value = "Получение комментариев на публикации", notes = "Получение комментариев на публикации")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение комментариев", response = CommentListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<CommentListResponse> getComments(
            @ApiParam(value = "ID публикации", defaultValue = "1")
            @PathVariable long id,
            @ApiParam(value = "Отступ от начала списка")
            @RequestParam(defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(defaultValue = "${default.perPage}") int itemPerPage,
            @RequestHeader("Authorization") String token
    ) throws AppException {
        return commentService.getComments(id, offset, itemPerPage, token);
    }

    @GetMapping("/post")
    @ApiOperation(value = "Поиск публикации", notes = "Поиск")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение публикации", response = PostListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PostListResponse> getPostSearch(
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "Текст публикации", defaultValue = "Бла бла")
            @RequestParam(name = "text", required = false) String textPublisher,
            @ApiParam(value = "Дата публикации ОТ", defaultValue = "1559751301818")
            @RequestParam(name = "date_from", required = false, defaultValue = "0") long datePublisherFrom,
            @ApiParam(value = "Дата публикации ДО", defaultValue = "1559751301818")
            @RequestParam(name = "date_to", required = false, defaultValue = "0") long datePublisherTo,
            @ApiParam(value = "Автор публикации", defaultValue = "Петр")
            @RequestParam(name = "author", required = false, defaultValue = "") String author,
            @ApiParam(value = "Тэг публикации", defaultValue = "класс")
            @RequestParam(name = "tags", required = false, defaultValue = "") String tags,
            @ApiParam(value = "Отступ от начала списка")
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(name = "itemPerPage", defaultValue = "0") Integer perPage
    ) throws UnAuthorizedException {
        return postService.getPostSearch(
                textPublisher,
                datePublisherFrom,
                datePublisherTo,
                author,
                tags,
                new PaginationSettings(offset, perPage),
                token);
    }

    @GetMapping("/post/{id}")
    @ApiOperation(value = "Получение публикаций по ID", notes = "Получение публикаций")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение публикации", response = PostResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PostResponse> getPostByID(@RequestHeader("Authorization") String token,
                                                    @ApiParam(value = "ID публикации", defaultValue = "1")
                                                    @PathVariable Long id) throws AppException {
        return postService.getPostById(id, token);
    }

    @PostMapping("/post/{id}/comments")
    @ApiOperation(value = "Создание комментария к публикации", notes = "Создание комментария к публикации")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное создание комментария", response = CommentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<CommentResponse> addComment(@ApiParam(value = "ID публикации", defaultValue = "1")
                                                      @PathVariable long id,
                                                      @RequestBody CommentDto commentDto,
                                                      @RequestHeader("Authorization") String token) throws AppException {
        return commentService.addComments(id, commentDto, token);
    }

    @PutMapping("/post/{id}/comments/{comment_id}")
    @ApiOperation(value = "Редактирование комментария к публикации", notes = "Редактирование комментария к публикации")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное создание комментария", response = CommentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<CommentResponse> updateComment(@RequestHeader("Authorization") String token,
                                                         @ApiParam(value = "ID публикации", defaultValue = "1")
                                                         @PathVariable long id,
                                                         @ApiParam(value = "ID коментария публикации", defaultValue = "1")
                                                         @PathVariable("comment_id") long commentId,
                                                         @RequestBody CommentDto commentDto) throws AppException {
        return commentService.updateComments(commentId, commentDto, token, id);
    }

    @DeleteMapping("/post/{id}/comments/{comment_id}")
    @ApiOperation(value = "Удаление комментария к публикации", notes = "Удаление комментария к публикации")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное создание комментария", response = RemoveCommentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<RemoveCommentResponse> removeComment(@RequestHeader("Authorization") String token,
                                                               @ApiParam(value = "ID публикации", defaultValue = "1")
                                                               @PathVariable long id,
                                                               @ApiParam(value = "ID коментария публикации", defaultValue = "1")
                                                               @PathVariable("comment_id") long commentId) throws BadRequestException {
        return commentService.removeComments(commentId, token, id);
    }

    @PutMapping("/post/{id}/comments/{comment_id}/recover")
    @ApiOperation(value = "Восстановление комментария", notes = "Восстановление комментария")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное восстановление комментария", response = CommentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<CommentResponse> recoverComment(@RequestHeader("Authorization") String token,
                                                          @ApiParam(value = "ID публикации", defaultValue = "1")
                                                          @PathVariable long id,
                                                          @ApiParam(value = "ID коментария публикации", defaultValue = "1")
                                                          @PathVariable("comment_id") long commentId) throws AppException {
        return commentService.recoverComment(id, commentId, token);
    }

    @PutMapping("/post/{id}")
    @ApiOperation(value = "Редактирование публикации по ID", notes = "Редактирование публикации по ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное редактирование публикации", response = PostResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<PostResponse> updatePost(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "ID публикации", defaultValue = "1")
            @PathVariable long id,
            @ApiParam(value = "Отложить до даты определенной даты", defaultValue = "1559751301818")
            @RequestParam(name = "publish_date", defaultValue = "0") long publishDate,
            @RequestBody AddPostRequest request
    ) throws AppException {
        return profileService.updatePost(id, publishDate, request, token);
    }

    //странный response
    @DeleteMapping("/post/{id}")
    @ApiOperation(value = "Удаление публикации по ID", notes = "Удаление публикации по ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное удаление публикации", response = RemoveCommentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> deletePost(
            @ApiParam(value = "ID публикации", defaultValue = "1")
            @PathVariable long id
    ) throws BadRequestException {
        return profileService.deletePost(id);
    }
}
