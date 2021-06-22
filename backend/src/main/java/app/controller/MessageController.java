package app.controller;

import app.dto.main.ErrorResponse;
import app.dto.messages.*;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import app.service.DialogService;
import app.service.MessageService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/dialogs")
@Api(tags = "Работа с диалогами")
public class MessageController {

    private final MessageService messageService;
    private final DialogService dialogService;

    public MessageController(MessageService messageService, DialogService dialogService) {
        this.messageService = messageService;
        this.dialogService = dialogService;
    }


    @PostMapping("/{id}/messages")
    @ApiOperation(value = "Отправка сообщений", notes = "Отправка сообщений")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешная отправка сообщения", response = MessageAddedResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageAddedResponse> postMessage(
            @ApiParam(value = "ID диалога", defaultValue = "3")
            @PathVariable int id,
            @ApiParam(value = "RequestBody")
            @RequestBody MessageRequest messageRequest,
            @RequestHeader("Authorization") String token) throws AppException {
        return messageService.postMessage(id, messageRequest, token);
    }



    @GetMapping("/{id}/messages")
    @ApiOperation(value = "Получить список сообщений в диалоге", notes = "Получение списка сообщений в диалоге")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка сообщений", response = MessagesByDialogResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessagesByDialogResponse> messagesByDialog(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "ID диалога", defaultValue = "1")
            @PathVariable int id,
            @ApiParam(value = "Строка для поиска сообщений", defaultValue = "Something")
            @RequestParam(required = false) String query,
            @ApiParam(value = "Отступ от начала списка")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "10")
            @RequestParam(required = false, defaultValue = "10") int itemPerPage
    ) throws UnAuthorizedException {
            return dialogService.getMessagesByDialog(token, id, query, offset, itemPerPage);
        }



    /* ДАЛЕЕ ЗАГЛУШКИ */

    @DeleteMapping("/{dialogId}/messages/{messageId}")
    @ApiIgnore
    public void deleteMessage(@PathVariable int dialogId, @PathVariable int messageId) {
        dialogService.printInfo("DELETE dialogs/{dialog_id}/messages/{message_id}");
    }

    @PutMapping("/{dialogId}/messages/{messageId}")
    @ApiIgnore
    public void editMessage(@PathVariable int dialogId, @PathVariable int messageId) {
        dialogService.printInfo("PUT dialogs/{dialog_id}/messages/{message_id}");
    }

    @PutMapping("/{dialogId}/messages/{messageId}/recover")
    @ApiIgnore
    public void recoverMessage(@PathVariable int dialogId, @PathVariable int messageId) {
        dialogService.printInfo("PUT dialogs/{dialog_id}/messages/{message_id}/recover");
    }

    @PutMapping("/{dialogId}/messages/{messageId}/read")
    @ApiIgnore
    public void markMessageAsRead(@PathVariable int dialogId, @PathVariable int messageId) {
        dialogService.printInfo("PUT dialogs/{dialog_id}/messages/{message_id}/read");
    }

}
