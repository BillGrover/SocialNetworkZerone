package app.controller;

import app.dto.main.ErrorResponse;
import app.dto.main.MessageResponse;
import app.dto.support.SupportOrderDto;
import app.dto.support.SupportRequest;
import app.exceptions.AppException;
import app.service.SupportService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/api/v1/support")
@Api(tags = "Обращение в службу поддержки")
public class SupportController {

    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @PostMapping("/message")
    @ApiOperation(value = "Получение сообщения", notes = "Получение сообщения из раздела Обращения в службу поддержки")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение сообщения"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> saveAppeal(
            @ApiParam(value = "Request Body")
            @RequestBody SupportRequest supportRequest,
            HttpServletRequest request)
            throws AppException {
        return supportService.saveAppeal(supportRequest, request);
    }

    @PostMapping("/answer/{orderId}")
    @ApiOperation(value = "Получение ответа", notes = "Получение ответа администратора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> saveAnswer(
            @ApiParam(value = " ID пользователя", defaultValue = "1")
            @PathVariable long orderId,
            @ApiParam(value = "text", defaultValue = "String")
            @RequestBody String text,
            @RequestHeader("Authorization") String token,
            HttpServletRequest request) throws AppException {
        return supportService.saveAnswer(orderId, text, token, request);
    }

    @GetMapping("/orders")
    @ApiOperation(value = "Получение списка образщений", notes = "Получение списка образщений в службу поддержки")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = SupportOrderDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<List<SupportOrderDto>> getOrderList(@RequestHeader("Authorization") String token)
            throws AppException {
        return supportService.getOrderList(token);
    }

    @GetMapping("/order/{orderId}")
    @ApiOperation(value = "Получение обращеня со списком", notes = "Получение обращеня со списком с ообщений")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = SupportOrderDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<SupportOrderDto> getMessageList(@ApiParam(value = " ID пользователя", defaultValue = "1")
                                                          @PathVariable long orderId,
                                                          @RequestHeader("Authorization") String token)
            throws AppException {
        return supportService.getOrderWithMessageList(orderId, token);
    }

    @PostMapping("/close/{id}")
    @ApiOperation(value = "Закрытие обращения", notes = "Закрытие обращения в службу поддержки")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное закрытие", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> closeOrder(@ApiParam(value = " ID пользователя", defaultValue = "1")
                                                          @PathVariable long id,
                                                      @RequestHeader("Authorization") String token,
                                                      HttpServletRequest request)
            throws AppException {
        return supportService.closeOrder(id, token, request);
    }

}