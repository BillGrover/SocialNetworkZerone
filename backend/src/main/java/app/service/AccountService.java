package app.service;

import app.dto.account.NotificationAccountDto;
import app.dto.account.NotificationListResponse;
import app.dto.account.RegistrationRequest;
import app.dto.main.MessageResponse;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccountService {

    ResponseEntity<MessageResponse> addNewUser(RegistrationRequest request, HttpServletRequest httpServletRequest)
            throws BadRequestException;

    String isApprove(String code, String token, HttpServletRequest request) throws BadRequestException;

    ResponseEntity<NotificationListResponse> getNotification(String token)
            throws AppException;

    ResponseEntity<MessageResponse> changeNotification(NotificationAccountDto request, String token)
            throws AppException;

    ResponseEntity<MessageResponse> changePassword(String token, HttpServletRequest request)
            throws AppException;

    ResponseEntity<MessageResponse> changeEmail(String token, HttpServletRequest request)
            throws AppException;

    ResponseEntity<MessageResponse> recoveryPassword(String email, HttpServletRequest request, HttpServletResponse response)
            throws BadRequestException;

    ResponseEntity<MessageResponse> setPassword(String password, String token, HttpServletRequest request)
            throws AppException;

    ResponseEntity<MessageResponse> setEmail(String email,
                                             String token,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws AppException;
}
