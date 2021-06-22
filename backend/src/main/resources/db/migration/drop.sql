
# Если что-то пошло не так, можно дропнуть все таблицы этим файлом

alter table block_history
    drop foreign key FKd6xnk2tlmih3iofeyu5myhipm;
alter table block_history
    drop foreign key FKs2nkvcw9gud7t27mgumo14hkp;
alter table block_history
    drop foreign key FKt4h1oocc1eeibw6m38lap04ro;
alter table friendship
    drop foreign key FKd7peios1ldfebilr8ia5h978x;
alter table friendship
    drop foreign key FK18m8yy9pp291xaqdjusvj9kxj;
alter table friendship
    drop foreign key FKnsoxkokd4koedao3127bvywju;
alter table messages
    drop foreign key FKimdd1pd5sipwsh4e5mepl7fcj;
alter table messages
    drop foreign key FKmbdxcyfputddn3xwmw1phto7t;
alter table post2tag
    drop foreign key FK3hxxi71po39189j9lc4wv93vm;
alter table post2tag
    drop foreign key FKc9yntlsu2li5iiyn3p7xlwfd3;
alter table post_comments
    drop foreign key FKf7t4v8dcs6fypin8iwartpv60;
alter table post_comments
    drop foreign key FKaawaqxjs3br8dw5v90w7uu514;
alter table post_files
    drop foreign key FK8xc9ew2bxk7axrmc9epq40v2h;
alter table post_likes
    drop foreign key FKa9oua9nyomg75dxgcpxaa8dtr;
alter table post_likes
    drop foreign key FKa5wxsgl4doibhbed9gm7ikie2;
alter table posts
    drop foreign key FK31biid9u6ekl7h6n2i7fgnqdq;
alter table recipients
    drop foreign key FKbtfmnv3joyqqh35ubgd1sue2c;
alter table recipients
    drop foreign key FKikb36t92tr9jql4gyb0957e0h;
alter table persons2roles
    drop foreign key FKc51o8nx4hs8oi8ce332ddhvg7;
alter table persons2roles
    drop foreign key FK3hlrdji5mcpij9isteablyovv;
alter table user_roles
    drop foreign key FKh8ciramu9cc9q3qcqiv4ue8a6;
alter table user_roles
    drop foreign key FKhfh9dx7w3ubf1co1vdev94g3f;
alter table persons
    drop foreign key FKhcy9b4brono9em0c6mk1lbh16;
alter table reset_token
    drop foreign key FKqkn3g9wcfxmphs5qjvd2cw2fw;
alter table messages
    drop foreign key dialog_to_messages;
alter table support_message
    drop foreign key FK418mqsrha5hgw6pmo4951wrmv;
alter table support_message
    drop foreign key FKluiuhbt4ihx0tsmhmqqjwt2ih;

drop table if exists block_history;
drop table if exists friendship;
drop table if exists friendship_status;
drop table if exists hibernate_sequence;
drop table if exists messages;
drop table if exists notifications;
drop table if exists post2tag;
drop table if exists post_files;
drop table if exists post_likes;
drop table if exists recipients;
drop table if exists tags;
drop table if exists user_roles;
drop table if exists users;
drop table if exists roles;
drop table if exists persons2roles;
drop table if exists flyway_schema_history;
drop table if exists notification_type;
drop table if exists persons;
drop table if exists reset_token;
drop table if exists post_comments;
drop table if exists posts;
drop table if exists persons;
drop table if exists cities;
drop table if exists countries;
drop table if exists dialogs;
drop table if exists notifications_settings;
drop table if exists admins;
drop table if exists support;
drop table if exists support_order;
drop table if exists support_message;

