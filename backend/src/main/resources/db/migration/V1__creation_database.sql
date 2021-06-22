create table block_history
(
    id              bigint      not null auto_increment,
    ban_status      varchar(255),
    time            datetime(6) not null,
    post_comment_id bigint,
    person_id       bigint,
    post_id         bigint,
    primary key (id)
) engine = InnoDB;

create table cities
(
    id         bigint       not null auto_increment,
    name       varchar(255) not null,
    country_id bigint,
    primary key (id)
) engine = InnoDB;

create table countries
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB;

create table friendship
(
    id                   bigint not null auto_increment,
    dst_person_id        bigint not null,
    src_person_id        bigint not null,
    friendship_status_id bigint not null,
    primary key (id)
) engine = InnoDB;

create table friendship_status
(
    id              bigint not null auto_increment,
    friendship_code varchar(255),
    name            varchar(255),
    time            datetime(6),
    primary key (id)
) engine = InnoDB;

create table hibernate_sequence
(
    next_val bigint
) engine = InnoDB;

insert into hibernate_sequence
values (1);

create table messages
(
    id                  bigint not null auto_increment,
    message_read_status varchar(255),
    text                varchar(255),
    time                datetime(6),
    author_id           bigint not null,
    recipient_id        bigint not null,
    primary key (id)
) engine = InnoDB;

create table notifications
(
    id                bigint not null auto_increment,
    contact           varchar(255),
    entity_id         bigint not null,
    notification_type varchar(255),
    time              datetime(6),
    primary key (id)
) engine = InnoDB;

create table persons
(
    id                 bigint not null auto_increment,
    about              varchar(255),
    approval           varchar(255),
    birth_date         datetime(6),
    block              varchar(255),
    confirmation_code  varchar(255),
    email              varchar(255),
    first_name         varchar(255),
    last_name          varchar(255),
    last_online_time   datetime(6),
    message_permission varchar(255),
    password           varchar(255),
    phone              varchar(255),
    photo              varchar(255),
    registration_date  datetime(6),
    city_id            bigint,
    country_id         bigint,
    primary key (id)
) engine = InnoDB;

create table post2tag
(
    id      integer not null auto_increment,
    post_id bigint  not null,
    tag_id  bigint  not null,
    primary key (id)
) engine = InnoDB;

create table post_comments
(
    id         bigint       not null auto_increment,
    is_blocked bit          not null,
    parent_id  integer      not null,
    text       varchar(255) not null,
    time       datetime(6)  not null,
    author_id  bigint       not null,
    post_id    bigint       not null,
    primary key (id)
) engine = InnoDB;

create table post_files
(
    id      integer      not null auto_increment,
    name    varchar(255),
    path    varchar(255) not null,
    post_id bigint,
    primary key (id)
) engine = InnoDB;

create table post_likes
(
    id        integer     not null auto_increment,
    time      datetime(6) not null,
    person_id bigint      not null,
    post_id   bigint      not null,
    primary key (id)
) engine = InnoDB;

create table posts
(
    id         bigint       not null auto_increment,
    is_blocked bit          not null,
    text       varchar(255) not null,
    time       datetime(6)  not null,
    title      varchar(255) not null,
    author_id  bigint,
    primary key (id)
) engine = InnoDB;

create table recipients
(
    notification_id bigint not null,
    person_id       bigint not null,
    primary key (notification_id, person_id)
) engine = InnoDB;

CREATE TABLE support
(
    id       bigint  not null auto_increment,
    email    varchar (255),
    message  varchar (255),
    name     varchar (255),
    primary key (id)
)engine = InnoDB;

create table tags
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB;

alter table block_history
    add constraint FKd6xnk2tlmih3iofeyu5myhipm foreign key (post_comment_id) references post_comments (id);
alter table block_history
    add constraint FKs2nkvcw9gud7t27mgumo14hkp foreign key (person_id) references persons (id);
alter table block_history
    add constraint FKt4h1oocc1eeibw6m38lap04ro foreign key (post_id) references posts (id);
alter table cities
    add constraint FK6gatmv9dwedve82icy8wrkdmk foreign key (country_id) references countries (id);
alter table friendship
    add constraint FKd7peios1ldfebilr8ia5h978x foreign key (dst_person_id) references persons (id);
alter table friendship
    add constraint FK18m8yy9pp291xaqdjusvj9kxj foreign key (src_person_id) references persons (id);
alter table friendship
    add constraint FKnsoxkokd4koedao3127bvywju foreign key (friendship_status_id) references friendship_status (id);
alter table messages
    add constraint FKimdd1pd5sipwsh4e5mepl7fcj foreign key (author_id) references persons (id);
alter table messages
    add constraint FKmbdxcyfputddn3xwmw1phto7t foreign key (recipient_id) references persons (id);
alter table persons
    add constraint FKhcy9b4brono9em0c6mk1lbh16 foreign key (city_id) references cities (id);
alter table persons
    add constraint FKerkc4hn581s5tffbfj3xlu864 foreign key (country_id) references countries (id);
alter table post2tag
    add constraint FK3hxxi71po39189j9lc4wv93vm foreign key (post_id) references posts (id);
alter table post2tag
    add constraint FKc9yntlsu2li5iiyn3p7xlwfd3 foreign key (tag_id) references tags (id);
alter table post_comments
    add constraint FKf7t4v8dcs6fypin8iwartpv60 foreign key (author_id) references persons (id);
alter table post_comments
    add constraint FKaawaqxjs3br8dw5v90w7uu514 foreign key (post_id) references posts (id);
alter table post_files
    add constraint FK8xc9ew2bxk7axrmc9epq40v2h foreign key (post_id) references posts (id);
alter table post_likes
    add constraint FKa9oua9nyomg75dxgcpxaa8dtr foreign key (person_id) references persons (id);
alter table post_likes
    add constraint FKa5wxsgl4doibhbed9gm7ikie2 foreign key (post_id) references posts (id);
alter table posts
    add constraint FK31biid9u6ekl7h6n2i7fgnqdq foreign key (author_id) references persons (id);
alter table recipients
    add constraint FKbtfmnv3joyqqh35ubgd1sue2c foreign key (person_id) references persons (id);
alter table recipients
    add constraint FKikb36t92tr9jql4gyb0957e0h foreign key (notification_id) references notifications (id);
