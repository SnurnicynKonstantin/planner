alter table "task" alter column "user_id" type int8;
alter table "task" alter column "user_change_id" type int8;
alter table "task" alter column "user_setter_id" type int8;

alter table "user" alter column "date_of_end_vacation" drop not null;