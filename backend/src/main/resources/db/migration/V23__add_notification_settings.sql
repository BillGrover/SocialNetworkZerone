create table notifications_settings
(
    id         bigint       not null auto_increment,
    type       varchar(255) not null,
    person_id bigint,
    primary key (id)
) engine = InnoDB;

alter table notifications add
    read_status  varchar(255) not null;

update notifications
set read_status = 'SENT';
