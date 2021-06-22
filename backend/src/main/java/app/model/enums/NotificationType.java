package app.model.enums;

public enum NotificationType {
    POST,               // О новых публикациях моих друзей
    POST_COMMENT,       // О новых комментариях к моим публикациям
    COMMENT_COMMENT,    // Об ответах на мои комментарии
    FRIEND_REQUEST,     // О заявках в друзья
    MESSAGE,            // О новых сообщениях
    FRIEND_BIRTHDAY,    // О дне рождения друга
    LIKE                // О лайках моим публикациям и комментариям
}
