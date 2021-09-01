drop table if exists role cascade;

CREATE TABLE "role" (
    "id" int8 not null,
    "name" varchar(255) NOT NULL unique,
    "skill_operations" boolean default false,
    "set_skill_to_user" boolean default false,
    "rate_skill" boolean default false,
    "watch_others_tasks" boolean default false,
    "tasks_operations" boolean default false,
    "edit_other_user" boolean default false,
    "import_skills" boolean default false,
    CONSTRAINT role_pk PRIMARY KEY ("id")
);

insert into role (id, name, skill_operations, set_skill_to_user, rate_skill, watch_others_tasks, tasks_operations, edit_other_user, import_skills)
values
(1, 'USER', false, false, false, false, false, false, false),
(2, 'ADMIN', true, true, true, true, true, true, true);

DO $$
BEGIN
  IF EXISTS(SELECT *
    FROM information_schema.columns
    WHERE table_name='user' and column_name='role_code')
  THEN
      ALTER TABLE "user" RENAME COLUMN role_code TO role_id;
  END IF;
END $$;

ALTER TABLE "user" ALTER COLUMN role_id TYPE int8;
alter table "user" drop constraint if exists user_role_fk;
alter table "user" add constraint user_role_fk foreign key (role_id) references role (id);
