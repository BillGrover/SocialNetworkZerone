alter table recipients
drop foreign key FKikb36t92tr9jql4gyb0957e0h;

drop table  notifications;


create table notifications
(
    id                bigint not null auto_increment,
    contact           varchar(255),
    person_id         bigint not null,
    entity_id         bigint not null,
    notification_type varchar(255),
    time              datetime(6),
    primary key (id)
) engine = InnoDB;

alter table recipients
    add constraint FKikb36t92tr9jql4gyb0957e0h foreign key (notification_id) references notifications (id);

