package app.service.implementations;

import app.config.EmailTemplate;
import app.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>РЕАЛИЗАЦИЯ СЕРВИСА ОТПРАВКИ ПИСЕМ</h2>
 * {@link EmailService Oбязательные функции}
 * <p>Для удобства можно использовать {@link EmailTemplate шаблоны писем}</p>
 */
@Service
public class EmailServiceImpl implements EmailService {

    /**
     * Инструмент для отпpавки электронной почты
     * <br> автоматически настраиватся по параметрам {@link app.config.EmailConfig конфигурации}
     */
    private final JavaMailSender mailSender;

    /**
     * Адрес электронной почты отправтеля, получаем из настроек проекта (application.yaml)
     */
    @Value("${email.username}")
    private String username;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Отправка тестового письма
     *
     * @param to адрес получателя
     */
    public void sendTestEmail(
            String to
    ) {
        sendRegistrationEmail(to, "link");
    }

    public void sendRecoveryPasswordEmail(
            String to,
            String link) {
        String bodyText = String.format(
                EmailTemplate.RECOVERY_PASSWORD_HTML.getTemplate(),
                link
        );
        sendHtmlEmail(to, "ZERONE: Восстановление пароля" , bodyText);
    }

    /**
     * Отправка письма для смены пароля
     * <br>используется {@link EmailTemplate#CHANGE_PASSWORD_HTML шаблон письма}
     *
     * @param to   адрес получаетля
     * @param link ссылка для смены пароля
     */
    public void sendChangePasswordEmail(
            String to,
            String link) {
        String bodyText = String.format(
                EmailTemplate.CHANGE_PASSWORD_HTML.getTemplate(),
                link
        );
        sendHtmlEmail(to, "ZERONE: Смена пароля", bodyText);
    }

    /**
     * Отправка письма для смены email
     * <br>используется {@link EmailTemplate#CHANGE_EMAIL_HTML шаблон письма}
     *
     * @param to   адрес получаетля
     * @param link ссылка для смены email
     */
    public void sendChangeEmailEmail(
            String to,
            String link) {
        String bodyText = String.format(
                EmailTemplate.CHANGE_EMAIL_HTML.getTemplate(),
                link
        );
        sendHtmlEmail(to, "ZERONE: Смена email", bodyText);
    }

    /**
     * Отправка письма для подтверждения регистрации
     * <br>используется {@link EmailTemplate#REGISTRATION_HTML html шаблон письма для регистрации}
     *
     * @param to          адрес получателя
     * @param confirmLink ссылка для подтверждения регистрации
     */
    public void sendRegistrationEmail(
            String to,
            String confirmLink
    ) {
        String bodyText = String.format(
                EmailTemplate.REGISTRATION_HTML.getTemplate(),
                to,
                confirmLink);
        sendHtmlEmail(to, "ZERONE: Подтверждение регистрации", bodyText);
    }

    public void sendSuccessEmail(
            String to,
            String loginLink
    ) {
        String bodyText = String.format(
                EmailTemplate.REGISTRATION_SUCCESS_HTML.getTemplate(),
                loginLink);
        sendHtmlEmail(to, "ZERONE: Успешная регистрация", bodyText);
    }

    public void sendSuccessResetEmail(
            String to,
            String loginLink
    ) {
        String bodyText = String.format(
                EmailTemplate.CHANGE_EMAIL_SUCCESS.getTemplate(),
                loginLink);
        sendHtmlEmail(to, "ZERONE: Успешная смена email", bodyText);
    }

    /**
     * Отправка простого письма без вложения
     * <br>ВАЖНО! В теле письма html-разметка не учитывается
     *
     * @param to       адрес получателя
     * @param subject  тема письма
     * @param bodyText тело письма (сообщение)
     */
    public void sendSimpleEmail(
            String to,
            String subject,
            String bodyText
    ) {
        if (notEmail(to)) return;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(username);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(bodyText);
        mailSender.send(email);
    }

    /**
     * Отправка простого письма без вдожения
     * <br>В теле писмьма можно использовать html-разметку
     *
     * @param to       адрес получателя
     * @param subject  тема письма
     * @param bodyText тело письма (сообщение)
     */
    public void sendHtmlEmail(
            String to,
            String subject,
            String bodyText
    ) {
        sendEmailWithAttachment(to, subject, bodyText, "");
    }

    /**
     * Отпрака письма с вложением
     *
     * @param to               адрес получателя
     * @param subject          тема письма
     * @param bodyText         тело письма (сообщение)
     * @param pathToAttachment прикрепляемый файл
     */
    public void sendEmailWithAttachment(
            String to,
            String subject,
            String bodyText,
            String pathToAttachment
    ) {
        if (notEmail(to)) return;
        MimeMessage email = mailSender.createMimeMessage();

        try {
            boolean attachmentCheck = pathToAttachment != null && !pathToAttachment.isEmpty();
            email.setContent(
                    new String(bodyText.getBytes(StandardCharsets.UTF_8)),
                    "text/html; charset=utf-8");
            MimeMessageHelper helper = new MimeMessageHelper(email, attachmentCheck, StandardCharsets.UTF_8.name());
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            if (attachmentCheck) {
                FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
                helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            }
            mailSender.send(email);
        } catch (
                MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Функция получения хоста и порта для ссылок
     * <br> Порт указывается только для localhost и 127.0.0.1
     *
     * @param request адрес запроса
     * @return строка в формате host<:port>
     */
    public String getHostAndPort(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * Функция для проверки адреса почты
     *
     * @param email адрес почты для проверки
     * @return true - не соответствует формату, не почта
     * <br>false - соответсвует, считаем что почта указана корректно
     */
    private boolean notEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._-]+@[A-Za-z0-9\\-]+(\\.[A-Za-z0-9\\-]{2,})+");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }
}
