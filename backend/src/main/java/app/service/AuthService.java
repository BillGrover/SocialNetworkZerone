package app.service;

import app.dto.auth.AuthRequest;
import app.dto.auth.AuthResponse;
import app.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    ResponseEntity<AuthResponse> authLogin(AuthRequest request, HttpServletResponse response) throws BadRequestException;

    void authLogout(HttpServletRequest request, HttpServletResponse response);

}
