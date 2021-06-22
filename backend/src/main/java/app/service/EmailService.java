package app.service;

import javax.servlet.http.HttpServletRequest;

/**
 * <h2>ОБЯЗАТЕЛЬНЫЕ ФУНКЦИИ ПОЧТОВОГО СЕРВИСА</h2>
 * Функция отправки простого письма
 * <br>Функция отправки письма с вложением
 */
public interface EmailService {

    void sendSimpleEmail(String to, String subject, String bodyText);

    void sendRegistrationEmail(String to, String confirmLink);

    void sendSuccessEmail(String to, String loginLink);

    void sendRecoveryPasswordEmail(String to, String link);

    void sendSuccessResetEmail(String to, String loginLink);

    void sendChangePasswordEmail(String to, String link);

    void sendChangeEmailEmail(String to, String link);

    void sendEmailWithAttachment(String to, String subject, String bodyText, String pathToAttachment);

    String getHostAndPort(HttpServletRequest request);

}