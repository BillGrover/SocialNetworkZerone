package app.controller;

import app.dto.FeedsResponseDto;
import app.dto.main.ErrorResponse;
import app.exceptions.UnAuthorizedException;
import app.service.PostService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер денты новостей
 *
 * @author Lev
 */
@RestController
@RequestMapping("/api/v1/feeds")
@Api(tags = "Работа с лентой новостей")
public class FeedsController {

    private final PostService postService;

    public FeedsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @ApiOperation(value = "Получение списка новостей", notes = "Работа с лентой новостей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка новостей", response = FeedsResponseDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<FeedsResponseDto> getFeeds(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "Текст новости для поиска")
            @RequestParam(required = false) String name,
            @ApiParam(value = "Отступ от начала списка")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(required = false, defaultValue = "10") int itemPerPage
    ) throws UnAuthorizedException {
        return postService.getPosts(name, offset, itemPerPage, token);
    }
}
