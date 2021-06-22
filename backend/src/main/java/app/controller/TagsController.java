package app.controller;

import app.dto.main.ErrorResponse;
import app.dto.main.MessageResponse;
import app.dto.tags.TagDto;
import app.dto.tags.TagResponse;
import app.dto.tags.TagListResponse;
import app.exceptions.BadRequestException;
import app.service.TagService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/tags")
@Api(tags = "Работа с тегами")
public class TagsController {
    private final TagService tagService;

    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @ApiOperation(value = "Получение тегов для публикации", notes = "Получение тегов")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение тегов", response = TagListResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<TagListResponse> getTags(@ApiParam(value = "Строчка для поиска по тегам")
                                                   @RequestParam String tag,
                                                   @ApiParam(value = "отступ от начала списка")
                                                   @RequestParam(defaultValue = "0") int offset,
                                                   @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
                                                   @RequestParam(defaultValue = "${default.perPage}") int itemPerPage) {
        return tagService.getTags(tag, offset, itemPerPage);
    }

    @PostMapping
    @ApiOperation(value = "Создание тега для публикации", notes = "Создание тега")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное создание тега", response = TagResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<TagResponse> addTag(@ApiParam(value = "Request body")
                                              @RequestBody TagDto tagDto) {
        return tagService.addTag(tagDto);
    }

    @DeleteMapping
    @ApiOperation(value = "Удаление", notes = "Удаление тега")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное удаление тега", response = MessageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> removeTag(@ApiParam(value = "ID тега") @RequestParam long id) throws BadRequestException {
        return tagService.removeTag(id);
    }
}
