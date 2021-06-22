package app.controller;

import app.aop.Loggable;
import app.dto.auth.AuthRequest;
import app.dto.auth.AuthResponse;
import app.dto.main.ErrorResponse;
import app.dto.main.MessageResponse;
import app.exceptions.BadRequestException;
import app.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Работа с авторизацией")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "Вход через логин, пароль", notes = "Вход через логин, пароль")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешный вход", response = AuthResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class)})
    @Loggable
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request,
            HttpServletResponse response)
            throws BadRequestException {
        return authService.authLogin(request, response);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "Выход", notes = "Выполнить выход")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешный выход", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class)})
    @Loggable
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.authLogout(request, response);
        return ResponseEntity.ok(MessageResponse.ok());
    }

}





