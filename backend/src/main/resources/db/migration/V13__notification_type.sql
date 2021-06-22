create table notification_type
(
    id                  bigint not null auto_increment,
    code                varchar(255),
    name                varchar(255),
    primary key (id)
) engine = InnoDB;

insert into notification_type(code, name)
    VALUES ('POST','Новый пост');
insert into notification_type(code, name)
    VALUES ('POST_COMMENT','Комментарий к посту');
insert into notification_type(code, name)
    VALUES ('COMMENT_COMMENT','Ответ на комментарий');
insert into notification_type(code, name)
    VALUES ('FRIEND_REQUEST','Запрос дружбы');
insert into notification_type(code, name)
    VALUES ('MESSAGE','Личное сообщение');

