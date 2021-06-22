INSERT INTO support_order (id, name, email, title, date, status)
VALUES (1, 'Иван','9824104901@mail.ru', 'Нужна помощь','2021-03-20 15:12:19.529000', 'OPEN');

INSERT INTO support_order (id, name, email, title, date, status)
VALUES (2, 'Николай', '9824104911@mail.ru', 'Привет','2021-03-23 15:12:19.529000', 'CLOSED');

INSERT INTO support_order (id, name, email, title, date, status)
VALUES (3, 'Петр','9824104921@mail.ru', 'Помощь нужна','2021-03-27 15:12:19.529000', 'NEW');


INSERT INTO support_message (id, date, order_id, admin_id, text)
VALUES (1, '2021-03-20 15:12:19.529000', 1, null, 'Помощь нужна');

INSERT INTO support_message (id, date, order_id, admin_id, text)
VALUES (2, '2021-03-20 16:12:19.529000', 1, null, 'Очень нужна помощь');

INSERT INTO support_message (id, date, order_id, admin_id, text)
VALUES (3, '2021-03-20 17:12:19.529000', 1, 1, 'Что случилось');

INSERT INTO support_message (id, date, order_id, admin_id, text)
VALUES (4, '2021-03-23 15:12:19.529000', 2, null , 'Опять нужна помощь');

INSERT INTO support_message (id, date, order_id, admin_id, text)
VALUES (5, '2021-03-23 16:12:19.529000', 2, 1 , 'Все ОК');

INSERT INTO support_message (id, date, order_id, admin_id, text)
VALUES (6, '2021-03-27 15:12:19.529000', 3, null , 'Привет техподдержка');