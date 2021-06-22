alter table block_history
drop foreign key FKd6xnk2tlmih3iofeyu5myhipm;

alter table post_comments
drop foreign key FKf7t4v8dcs6fypin8iwartpv60;
alter table post_comments
drop foreign key FKaawaqxjs3br8dw5v90w7uu514;

alter table post_likes
drop foreign key FKa9oua9nyomg75dxgcpxaa8dtr;
alter table post_likes
drop foreign key FKa5wxsgl4doibhbed9gm7ikie2;

drop table if exists post_comments;

drop table if exists post_likes;

create table post_comments
(
    id         bigint       not null auto_increment,
    block varchar(20) DEFAULT 'UNBLOCKED',
    parent_id  bigint      ,
    text       varchar(255) not null,
    time       datetime(6)  not null,
    author_id  bigint       not null,
    post_id    bigint       ,
    delete_status enum('DELETED', 'UNDELETED') DEFAULT 'UNDELETED' NOT NULL,
    delete_date datetime(6),
    primary key (id)
);

create table post_likes
(
    id        bigint     not null auto_increment,
    time      datetime(6) not null,
    person_id bigint      not null,
    post_id   bigint      ,
    post_comments_id bigint,
    primary key (id)
);

alter table post_comments
    add constraint FKcomments_of_person foreign key (author_id) references persons (id);
alter table post_comments
    add constraint FKcomments_for_post foreign key (post_id) references posts (id);
alter table post_comments
    add constraint FKconnectionComToParentCom foreign key (parent_id) references post_comments (id);
alter table block_history
    add constraint FKd6xnk2tlmih3iofeyu5myhipm foreign key (post_comment_id) references post_comments (id);


alter table post_likes
    add constraint FKlike_of_person foreign key (person_id) references persons (id);
alter table post_likes
    add constraint FKlike_to_post foreign key (post_id) references posts (id);
alter table post_likes
    add constraint FKlike_to_comment foreign key (post_comments_id) references post_comments (id);

INSERT INTO post_comments (parent_id, text, time, author_id, post_id, block) VALUES (null, 'Текст коммента', '2020/11/26', 2, 12, 'UNBLOCKED');
INSERT INTO post_comments (parent_id, text, time, author_id, post_id, block) VALUES (null, 'Текст коммента', '2020/11/07', 2, 1, 'UNBLOCKED');
INSERT INTO post_comments (parent_id, text, time, author_id, post_id, block) VALUES (null, 'Текст коммента', '2020/01/08', 4, 23, 'UNBLOCKED');
INSERT INTO post_comments (parent_id, text, time, author_id, post_id, block) VALUES (3, 'Текст коммента в ответ на коммент', '2020/01/08', 1, 23, 'UNBLOCKED');
INSERT INTO post_comments (parent_id, text, time, author_id, post_id, block) VALUES (null, 'Текст коммента', '2020/11/25', 2, 24, 'UNBLOCKED');
INSERT INTO post_comments (parent_id, text, time, author_id, post_id, block) VALUES (null, 'Текст коммента', '2020/11/21', 1, 37, 'UNBLOCKED');
