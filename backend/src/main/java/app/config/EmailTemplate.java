package app.config;

/**
 * <h2>ШАБЛОНЫ ПИСЕМ</h2>
 * <p>В шаблоны для подтверждения регистрации необходимо подставить два параметра:
 * <br> email - адрес почты, который указали при регтстрации
 * <br> link - ссылка для подтвержения</p>
 * <p>В шаблон удачной регистрации - один параметр:
 * <br> link - ссылка на страницу регистрации</p>
 * <p> В шаблоны HTML ссылка подставляетс в виде кнопки.
 * <br> Формат кнопки задетеся в {@link EmailTemplate#getButton(String) отдельном методе}</p>
 *
 * @author Алексей Маслов
 */

public enum EmailTemplate {
    REGISTRATION("%nЗдравствуйте!" +
            "%nВы получили это письмо, потому что адрес %s был указан при регистрации в сети ZERONE." +
            "%nЕсли это были не вы, то просто проигнорируйте это письмо.%n%n" +
            "%nДля подтверждения регистрации перейдите по ссылке:%n%s"),
    REGISTRATION_HTML("<p>Здравствуйте!</p>" +
            "<p>Вы получили это письмо потому, что адрес %s был указан при регистрации в сети ZERONE.</p>" +
            "<p style=\"margin-bottom: 3em;\">Если это были не вы, то просто проигнорируйте это письмо.</p>" +
            "<p>Для подтверждения регистрации нажмите кнопку ниже<p>" +
            getButton("Подтвердить регистрацию")),
    REGISTRATION_SUCCESS("%nПоздравляем!" +
            "%nВы зарегистрировались в сети ZERONE.%n%n" +
            "Для входа перейдите по ссылке:%n%s"),
    REGISTRATION_SUCCESS_HTML("<p>Поздравляем!</p>" +
            "<p style=\"margin-bottom: 3em;\">Вы зарегистрировались в сети ZERONE.</p>" +
            getButton("Войти в личный кабинет")),
    CHANGE_PASSWORD_HTML("<p>Для смены пароля нажмите на кнопку</p>" + getButton("Сменить пароль")),
    CHANGE_EMAIL_HTML("<p>Для смены email нажмите на кнопку</p>" + getButton("Сменить email")),
    RECOVERY_PASSWORD_HTML("<p>Для восстановления пароля нажмите на кнопку</p>" + getButton("Восстановить пароль")),
    CHANGE_EMAIL_SUCCESS("<p>Поздравляем!</p>" +
            "<p style=\"margin-bottom: 3em;\">Вы успешно сменили EMAIL.</p>" +
            getButton("Войти в личный кабинет")),
    SUPPORT_BODY("<p>Здравствуйте!</p>" +
            "<p>Вы получили это письмо, потому что ваш адрес был указан в обращении в службу поддержки сети ZERONE.</p>" +
            "<p style=\"margin-bottom: 3em;\">Если это были не вы, то просто проигнорируйте это письмо.</p>" +
            "<div style=\"max-width: 520px; margin: 0 0 3em;\">" +
            "<h2 style=\"color: #fff;  background-color: #21A45D; font-family: sans-serif; " +
            "padding: 10px 20px; max-width: 520px\">Обращение #%d</h2>" +
            "<div style=\"\">%s</div></div>" +
            "<p></p>" +
            "<p>Если вы хотите что-то добавить, отправьте новое сообщение " +
            "с указанием тех же персональных данных</p>" + getButton("Отправить сообщение") +
            "<p></p>" +
            "<p>С уважением, служба поддержки ZERONE</p>"),
    SUPPORT_ROW("<div style=\"max-width: 320px; padding: 10px 30px; margin-bottom: 10px;" +
            "background-color: #f1f3f9; border-radius: 10px;\">" +
            "<p style=\"color: #21A45D; font-weight: bold; margin: 0 0 5px;\">Вы:</p>" +
            "<p style=\"margin: 0 0 5px;\">%s</p>" +
            "<p style=\"font-size: 8px; margin: 0; text-align: right; \">%s</p></div>"),
    SUPPORT_ROW_GREEN("<div style=\"max-width: 320px; padding: 10px 30px; margin: 0 0 10px auto;" +
            "color: #fff; background-color: #21A45D; border-radius: 10px;\">" +
            "<p style=\"font-weight: bold; margin: 0 0 5px;\">%s</p>" +
            "<p style=\"margin: 0 0 5px;\">%s</p>" +
            "<p style=\"font-size: 8px; margin: 0; text-align: right; \">%s</p></div>");


    /**
     * Поле шаблона
     */
    private final String template;

    /**
     * Конструктор для добавления текста шаблона к названию
     *
     * @param template текст шаблона
     */
    EmailTemplate(String template) {
        this.template = template;
    }

    /**
     * Функция получения поля шаблона {@link app.config.EmailTemplate#template}
     *
     * @return возвращает строку-шаблон
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Функция создает html-код кнопки с текстом для шаблона письма
     *
     * @param text текст на кнопке
     * @return возвращает строку с html-кодом кнопки
     */
    private static String getButton(String text) {
        return "<a href=\"%s\" " +
                "style=\"" +
                "display: inline-block; margin-top: 10px; padding: 20px 40px; font-family: sans-serif; " +
                "text-decoration: none; color: #fff; background-color: #00b053; cursor: pointer; " +
                "font-weight: 700; letter-spacing: 1px;\">" +
                text + "</a>";
    }
}
