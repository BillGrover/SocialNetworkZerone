CREATE TABLE admins
(
    id       bigint                      not null auto_increment,
    email    varchar(255),
    name     varchar(255),
    password varchar(255),
    status   VARCHAR(20) DEFAULT 'ADMIN' NOT NULL,
    primary key (id)
);

INSERT INTO admins (email, name, password, status)
VALUES ('vas_pup@yandex.ru', 'Вася Пупкин', '12345678', 'ADMIN');

INSERT INTO admins (email, name, password, status)
VALUES ('van_sid@mail.ru', 'Ленивец Сид', '12345678', 'MODERATOR');
