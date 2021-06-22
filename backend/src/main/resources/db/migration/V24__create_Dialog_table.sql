CREATE TABLE dialogs
(
    id                int not null auto_increment,
    unread_count      int,
    dialog_starter_id int,
    dialog_target_id  int,
    primary key (id)
) engine = InnoDB;

ALTER TABLE messages
    ADD dialog_id INT;

ALTER TABLE messages
    ADD CONSTRAINT dialog_to_messages FOREIGN KEY (dialog_id) REFERENCES dialogs (id);
