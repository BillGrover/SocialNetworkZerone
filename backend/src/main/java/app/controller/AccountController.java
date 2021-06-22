package app.controller;

import app.dto.account.NotificationAccountDto;
import app.dto.account.NotificationListResponse;
import app.dto.account.RegistrationRequest;
import app.dto.change.ChangeEmailDto;
import app.dto.change.ChangePasswordDto;
import app.dto.main.ErrorResponse;
import app.dto.main.MessageResponse;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.model.ResetToken;
import app.repository.ResetTokenRepository;
import app.service.AccountService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/account")
@Api(tags = "Работа с паролями/идентификаторами пользователя")
public class AccountController {

    private static final String ERROR = "error";
    private static final String TOKEN = "token";

    private final AccountService accountService;
    private final ResetTokenRepository tokenRepository;

    public AccountController(AccountService accountService, ResetTokenRepository tokenRepository) {
        this.accountService = accountService;
        this.tokenRepository = tokenRepository;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Регистрация", notes = "Регистрация пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешная регистрация", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> register(
            @ApiParam(value = "Request body")
            @RequestBody RegistrationRequest request,
            HttpServletRequest httpServletRequest) throws BadRequestException {
        return accountService.addNewUser(request, httpServletRequest);
    }

    @GetMapping("/notifications")
    @ApiOperation(value = "Получить список уведомлений", notes = "Получить список уведомлений")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение списка уведомлений"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<NotificationListResponse> getNotifications(
            @RequestHeader("Authorization") String token) throws AppException {
        return accountService.getNotification(token);
    }


    @PutMapping("/notifications")
    @ApiOperation(value = "Редактирование настроек оповещения", notes = "Редактирование настроек оповещения")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешная смена статуса", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> changeNotifications(
            @ApiParam(value = "Request body")
            @RequestBody NotificationAccountDto request,
            @RequestHeader("Authorization") String token) throws AppException {
        return accountService.changeNotification(request, token);
    }

    @RequestMapping("/approve")
    @ApiIgnore
    public String getApproval(
            @RequestParam String code,
            @RequestParam String token,
            HttpServletRequest request) throws BadRequestException {
        return accountService.isApprove(code, token, request);
    }

    @PutMapping("/password/change")
    @ApiOperation(value = "Редактирование пароля текущего пользователя", notes = "Редактирование пароля текущего пользователя")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Успешное получение пароля текущего пользователя",
                    response = MessageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> changePassword(
            @RequestHeader("Authorization") String token,
            HttpServletRequest request)
            throws AppException {
        return accountService.changePassword(token, request);
    }

    @PutMapping("/email/change")
    @ApiOperation(value = "Редактирование email'a текущего пользователя", notes = "Редактирование email'a текущего пользователя")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Успешное получение email текущего пользователя",
                    response = MessageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> changeEmail(
            @RequestHeader("Authorization") String token,
            HttpServletRequest request)
            throws AppException {
        return accountService.changeEmail(token, request);
    }

    @PutMapping("/password/recovery")
    @ApiOperation(value = "Восстановление пароля текущего пользователя", notes = "Восстановление пароля текущего пользователя")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Успешное получение пароля текущего пользователя",
                    response = MessageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> changePassword(
            @RequestBody ChangeEmailDto changeEmailDto, HttpServletRequest request, HttpServletResponse response)
            throws BadRequestException {
        return accountService.recoveryPassword(changeEmailDto.getEmail(), request, response);
    }

    @PutMapping("/password/set")
    @ApiOperation(value = "Смена пароля пользователя", notes = "Смена пароля пользователя")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Успешное получение пароля текущего пользователя",
                    response = MessageResponse.class)})
    public ResponseEntity<MessageResponse> setPassword(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request)
            throws AppException {
        return accountService.setPassword(changePasswordDto.getPassword(), token, request);
    }

    @PutMapping("/email")
    @ApiOperation(value = "Смена email'а пользователя", notes = "Смена email'а пользователя")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Успешное получение email текущего пользователя",
                    response = MessageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> setPassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangeEmailDto changeEmailDto, HttpServletRequest request, HttpServletResponse response)
            throws AppException {
        return accountService.setEmail(changeEmailDto.getEmail(), token, request, response);
    }

    @GetMapping("/reset-email")
    @ApiIgnore
    public String displayResetEmailPage(@RequestParam(required = false) String token, Model model) {
        return changePassword(token, model) ? "redirect:/shift-email" : ERROR;
    }

    @GetMapping("/reset-password")
    @ApiIgnore
    public String displayResetPasswordPage(@RequestParam(required = false) String token, Model model) {
        return changePassword(token, model) ? "redirect:/shift-password" : ERROR;
    }

    @GetMapping("/forgot-password")
    @ApiIgnore
    public String displayForgotPasswordPage(@RequestParam(required = false) String token, Model model) {
        return changePassword(token, model) ? "redirect:/change-password" : ERROR;
    }

    private boolean changePassword(String token, Model model) {
        Optional<ResetToken> resetToken = tokenRepository.findByToken(token);
        if (resetToken.isEmpty()) {
            model.addAttribute(ERROR, "Не удалось выполнить восстановление пароля.");
            return false;
        } else if (resetToken.get().isExpired()) {
            model.addAttribute(ERROR, "Срок действия восстановления пароля истек. Повторите процедуру восстановления.");
            tokenRepository.delete(resetToken.get());
            return false;
        } else {
            model.addAttribute(TOKEN, resetToken.get().getToken());
        }
        tokenRepository.delete(resetToken.get());
        return true;
    }
}