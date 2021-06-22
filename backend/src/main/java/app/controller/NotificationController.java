package app.controller;

import app.dto.main.ErrorResponse;
import app.dto.notification.NotificationListResponse;
import app.exceptions.AppException;
import app.service.NotificationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@Api(tags = "Работа с уведомлениями")
public class NotificationController {

    private final NotificationService notificationServiceImpl;

    @Autowired
    public NotificationController(NotificationService notificationServiceImpl) {
        this.notificationServiceImpl = notificationServiceImpl;
    }

    @GetMapping
    @ApiOperation(value = "Получить список уведомлений", notes = "Получить список уведомлений для текущего пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка уведомлений", response = NotificationListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<NotificationListResponse> getNotification(
           /* @ApiParam(value = "Отступ от начала списка", defaultValue = "0")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @ApiParam(value = "Количество элементов на страницу", defaultValue = "10")
            @RequestParam(required = false, defaultValue = "10") int itemPerPage,*/
            @RequestHeader("Authorization") String token
    ) throws AppException {

        return notificationServiceImpl.notificationAll(/*offset,itemPerPage,*/ token);
    }


    @PutMapping
    @ApiOperation(value = "Прочитать уведомление", notes = "Отметить уведомление как \"прочитанное\"")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка уведомлений", response = NotificationListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<NotificationListResponse> putNotification(
            @ApiParam(value = "ID уведомлен", defaultValue = "1")
            @RequestParam(required = false, defaultValue = "0") long id,
            @ApiParam(value = "Пометка что прочесть все уведомления", allowEmptyValue = true)
            @RequestParam(required = false, defaultValue = "false") boolean all,
            @RequestHeader("Authorization") String token
    ) throws AppException {

        return notificationServiceImpl.notificationDelete(id,all, token);
    }
}
