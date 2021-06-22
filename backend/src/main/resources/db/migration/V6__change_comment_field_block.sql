alter table post_comments drop column is_blocked;
alter table post_comments add block varchar(20);
alter table post_comments alter block SET DEFAULT 'UNBLOCKED';