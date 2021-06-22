package app.service;

import app.dto.notification.NotificationListResponse;
import app.exceptions.AppException;
import org.springframework.http.ResponseEntity;

public interface NotificationService {

    ResponseEntity<NotificationListResponse> notificationAll(String token) throws AppException;

    ResponseEntity<NotificationListResponse> notificationDelete(long id, boolean all, String token) throws AppException;


}
