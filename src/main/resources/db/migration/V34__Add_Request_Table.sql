DROP TABLE IF EXISTS request CASCADE;
DROP TABLE IF EXISTS request_comment CASCADE;

CREATE TABLE request (
    "id" int8 NOT NULL PRIMARY KEY,
    "status" varchar(255) NOT NULL,
    "category" varchar(255) NOT NULL,
    "create_date" TIMESTAMP NOT NULL,
    "update_date" TIMESTAMP NOT NULL,
    "creator_id" int8 NOT NULL REFERENCES "user" ("id"),
    "executor_id" int8 REFERENCES "user" ("id"),
    "title" varchar(255) NOT NULL,
    "description" varchar(255)
);

CREATE TABLE request_comment (
    "id" int8 NOT NULL PRIMARY KEY,
    "create_date" TIMESTAMP NOT NULL,
    "author_id" int8 NOT NULL REFERENCES "user" ("id"),
    "request_id" int8 NOT NULL REFERENCES "request" ("id"),
    "text" varchar(255) NOT NULL
);

alter table role
    add column if not exists request_operations boolean default false;

update role
set request_operations = true
where name = 'ADMIN';

update role
set request_operations = false
where name = 'USER';