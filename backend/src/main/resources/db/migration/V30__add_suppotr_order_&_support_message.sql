create table support_order
(
    id         bigint       not null auto_increment,
    name       varchar(255),
    email      varchar(255),
    title      varchar(255),
    date       datetime(6),
    status     varchar(255),
    primary key (id)
) engine = InnoDB;

create table support_message
(
    id         bigint       not null auto_increment,
    date       datetime(6),
    order_id   bigint,
    admin_id   bigint,
    text       varchar(255),
    primary key (id)
) engine = InnoDB;

alter table support_message add
    constraint FK418mqsrha5hgw6pmo4951wrmv foreign key (admin_id) references admins (id);
alter table support_message add
    constraint FKluiuhbt4ihx0tsmhmqqjwt2ih foreign key (order_id) references support_order (id);