create table reset_token
(
    id          bigint not null auto_increment,
    token       varchar(255) unique,
    expiry_date datetime(6),
    person_id   bigint,
    primary key (id)
) engine = InnoDB;

alter table reset_token
    add constraint FK1111 foreign key (person_id) references persons (id);