# Пользователи, которые не ввели персональные данные

insert into persons (email, first_name, last_name, approval, block, password, confirmation_code, registration_date, photo, message_permission)
values ('scr1@mail.ru',
        'Максим',
        'Скромняга',
        'APPROVED',
        'UNBLOCKED',
        '$2y$12$H37gSt8zlH1qkPqYVPByXO2bsSVkcDfSgwcJlyTobzdLPvRx3yzne',
        'confirmation',
        '2021/03/05',
        '/static/img/default_avatar.png',
        'ALL');

insert into persons (email, first_name, last_name, approval, block, password, confirmation_code, registration_date, photo, message_permission)
values ('scr2@mail.ru',
        'Мария',
        'Скромняга',
        'APPROVED',
        'UNBLOCKED',
        '$2y$12$H37gSt8zlH1qkPqYVPByXO2bsSVkcDfSgwcJlyTobzdLPvRx3yzne',
        'confirmation',
        '2021/03/05',
        '/static/img/default_avatar.png',
        'ALL');


# Пользователи не подтвердившие регистрацию

insert into persons (email, first_name, last_name, approval, block, password, confirmation_code, registration_date, photo, message_permission)
values ('rej1@mail.ru',
        'Иван',
        'Нерешительный',
        'REJECTED',
        'BLOCKED',
        '$2y$12$H37gSt8zlH1qkPqYVPByXO2bsSVkcDfSgwcJlyTobzdLPvRx3yzne',
        'confirmation',
        '2021/03/05',
        '/static/img/default_avatar.png',
        'ALL');

insert into persons (email, first_name, last_name, approval, block, password, confirmation_code, registration_date, photo, message_permission)
values ('rej2@mail.ru',
        'Вероника',
        'Задумчивая',
        'REJECTED',
        'BLOCKED',
        '$2y$12$H37gSt8zlH1qkPqYVPByXO2bsSVkcDfSgwcJlyTobzdLPvRx3yzne',
        'confirmation',
        '2021/03/05',
        '/static/img/default_avatar.png',
        'ALL');


# Удаленные пользователи

insert into persons (first_name, last_name, approval, block, phone, photo, about)
values ('No',
        'Name',
        'DELETED',
        'BLOCKED',
        '-',
        '/static/img/deleted_avatar.png',
        'Пользователь удален 05-03-2011');

insert into persons (first_name, last_name, approval, block, phone, photo, about)
values ('No',
        'Name',
        'DELETED',
        'BLOCKED',
        '-',
        '/static/img/deleted_avatar.png',
        'Пользователь удален 05-03-2011');

