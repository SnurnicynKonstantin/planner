alter table "user_skill" add if not exists "version" int default 1;
alter table "user_skill" drop constraint if exists "user_skill_user_id_skill_id_key";
alter table "user_skill" drop constraint if exists "user_skill_user_id_skill_id_version_key";
alter table "user_skill" add constraint "user_skill_user_id_skill_id_version_key" unique (user_id, skill_id, version);