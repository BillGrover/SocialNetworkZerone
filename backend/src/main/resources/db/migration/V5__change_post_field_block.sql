alter table posts drop column is_blocked;
alter table posts add block varchar(20);
alter table posts alter block SET DEFAULT 'UNBLOCKED';