package app.controller;

import app.dto.dialogs.*;
import app.dto.main.ErrorResponse;
import app.dto.messages.UnreadMessagesCountResponse;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import app.service.DialogService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/dialogs")
@Api(tags = "Работа с диалогами")
public class DialogController {

    DialogService dialogService;

    public DialogController(DialogService dialogService) {
        this.dialogService = dialogService;
    }


    @GetMapping
    @ApiOperation(value = "Получить список диалогов", notes = "Получение списка диалогов пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка Диалогов", response = DialogResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<DialogResponse> allDialogs(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "Строка для поиска диалога", defaultValue = "Something")
            @RequestParam(required = false) String query,
            @ApiParam(value = "Отступ от начала списка")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "20")
            @RequestParam(required = false, defaultValue = "10") int itemPerPage) throws AppException {

        return dialogService.getDialogs(token, query, offset, itemPerPage);
    }


    @PostMapping
    @ApiOperation(value = "Создать диалог", notes = "Создание диалога")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное создание Диалога", response = DialogPostedResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<DialogPostedResponse> createDialog(@ApiParam(value = "RequestBody")
                                                             @RequestBody DialogRequest dialogRequest,
                                                             @RequestHeader("Authorization") String token
    ) throws UnAuthorizedException {
        return dialogService.createDialog(dialogRequest, token);
    }


    @GetMapping("/unreaded")
    @ApiOperation(value = "Получение общего кол-ва непрочитанных сообщений", notes = "Получение общего кол-ва непрочитанных сообщений")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение общего кол-ва непрочитанных сообщений", response = UnreadMessagesCountResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<UnreadMessagesCountResponse> unreadMessages(@RequestHeader("Authorization") String token) throws UnAuthorizedException {
        return dialogService.getUnreadMessages(token);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удалить диалог", notes = "Удаление диалога")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное удаление Диалога", response = DialogPostedResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<DialogPostedResponse> deleteDialog(@ApiParam(value = "ID диалога", defaultValue = "8")
                                                             @PathVariable int id) {
        return dialogService.deleteDialog(id);
    }




    /* ДАЛЕЕ ЗАГЛУШКИ */

    /**
     * Добавить пользователя в диалог
     */
    @PutMapping("/{id}/users")
    @ApiIgnore
    public void addUsersIntoDialog(@RequestBody DialogRequest dialogRequest, @PathVariable int id) {
        dialogService.printInfo("PUT dialogs/{id}/users");
    }

    /**
     * Удалить пользователей из диалога
     */
    @DeleteMapping("/{id}/users")
    @ApiIgnore
    public void deleteUsersFromDialog(@PathVariable int id) {
        dialogService.printInfo("DELETE dialogs/{id}/users");
    }

    /**
     * Получить ссылку-приглашение в диалог
     */
    @GetMapping("/{id}/users/invite")
    @ApiIgnore
    public void invite(@PathVariable int id) {
        dialogService.printInfo("GET dialogs/{id}/users/invite");
    }

    /**
     * Присоедениться к диалогу по ссылке-приглашению
     */
    @PutMapping("/{id}/users/join")
    @ApiIgnore
    public void join(@PathVariable int id) {
        dialogService.printInfo("PUT dialogs/{id}/users/join");
    }

    /**
     * Получить последнюю активность и текущий статус для пользователя с которым ведется диалог.
     */
    @GetMapping("/{id}/activity/{userId}")
    @ApiIgnore
    public void getActivity(@PathVariable int id, @PathVariable int userId) {
        dialogService.printInfo("GET dialogs/{id}/activity/{user_id}");
    }

    /**
     * Изменить статус набора текста пользователем в диалоге.
     */
    @PostMapping("/{id}/activity/{userId}")
    @ApiIgnore
    public void setMessageTypingStatus(@PathVariable int id, @PathVariable int userId) {
        dialogService.printInfo("POST dialogs/{id}/activity/{user_id}");
    }

    /**
     * Получить данные для подключения к longpoll серверу
     */
    @GetMapping("/longpoll")
    @ApiIgnore
    public void longpoll() {
        dialogService.printInfo("GET dialogs/longpoll");
    }

    /**
     * Получить обновления личных сообщений пользователя
     */
    @PostMapping("/longpoll/history")
    @ApiIgnore
    public void longpollHistory() {
        dialogService.printInfo("POST dialogs/longpoll/history");
    }

}
