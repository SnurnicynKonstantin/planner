ALTER TABLE "user"
DROP COLUMN IF EXISTS patronymic;

ALTER TABLE "user"
ADD COLUMN IF NOT EXISTS login varchar(255);

alter table "contact"
alter column email drop not null;

alter table "contact"
alter column phone_number drop not null;

alter table "contact"
drop constraint contact_phone_number_key;

alter table "contact"
drop constraint contact_email_key;


