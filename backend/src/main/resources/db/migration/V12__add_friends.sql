
INSERT INTO  friendship_status(friendship_code,name,time)
VALUES('REQUEST','',now());
INSERT INTO  friendship_status(friendship_code,name,time)
VALUES('FRIEND','',now());
INSERT INTO  friendship_status(friendship_code,name,time)
VALUES('SUBSCRIBED','',now());
INSERT INTO  friendship_status(friendship_code,name,time)
VALUES('FRIEND','',now());
INSERT INTO  friendship_status(friendship_code,name,time)
VALUES('BLOCKED','',now());
INSERT INTO  friendship_status(friendship_code,name,time)
VALUES('FRIEND','',now());
INSERT INTO  friendship_status(friendship_code,name,time)
VALUES('FRIEND','',now());


INSERT INTO friendship(dst_person_id,src_person_id,friendship_status_id)
VALUES(1,2,1);
INSERT INTO friendship(dst_person_id,src_person_id,friendship_status_id)
VALUES(1,3,2);
INSERT INTO friendship(dst_person_id,src_person_id,friendship_status_id)
VALUES(3,1,6);
INSERT INTO friendship(dst_person_id,src_person_id,friendship_status_id)
VALUES(1,4,3);
INSERT INTO friendship(dst_person_id,src_person_id,friendship_status_id)
VALUES(2,3,4);
INSERT INTO friendship(dst_person_id,src_person_id,friendship_status_id)
VALUES(3,2,7);
INSERT INTO friendship(dst_person_id,src_person_id,friendship_status_id)
VALUES(4,2,5);