alter table recipients
drop foreign key FKikb36t92tr9jql4gyb0957e0h;

alter table recipients
drop foreign key FKbtfmnv3joyqqh35ubgd1sue2c;

alter table notifications
    add constraint FKbtfmnv3joyqqh35ubgd1sue2c foreign key (person_id) references persons (id);

INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',1,1,"POST",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',2,5,"POST",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',3,5,"POST",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',2,1,"FRIEND_REQUEST",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',3,2,"FRIEND_REQUEST",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',1,3,"MESSAGE",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',1,2,"FRIEND_BIRTHDAY",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',1,1,"LIKE",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',2,1,"LIKE",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',3,1,"LIKE",now());
INSERT INTO notifications(contact,person_id,entity_id,notification_type,time)
VALUES('',4,1,"LIKE",now());

