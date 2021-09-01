DROP TABLE IF EXISTS "request_category" CASCADE;

CREATE TABLE "request_category"
(
    "id" int8 NOT NULL PRIMARY KEY,
    "name" varchar(255) NOT NULL UNIQUE,
    "description" varchar(255)
);

insert into request_category(id, name, description)
    values
        (1, 'UNDEFINED', 'Не определено'),
        (2, 'TECHSUPPORT', 'Техподдержка'),
        (3, 'ADMINISTRATIVE', 'Административная');


ALTER TABLE "request" DROP COLUMN if exists "category";
ALTER TABLE "request" ADD COLUMN IF NOT EXISTS "category_id" int8 NOT NULL REFERENCES "request_category" ("id") DEFAULT 1;