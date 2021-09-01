DROP TABLE IF EXISTS "group" CASCADE;

CREATE TABLE "group"
(
    "id"   int8       NOT NULL,
    "name" varchar(255) NOT NULL UNIQUE,
    CONSTRAINT "group_pk" PRIMARY KEY ("id")
);

ALTER TABLE "skill" ADD "group_id" int8 NOT NULL;
ALTER TABLE "skill" ADD FOREIGN KEY ("group_id") REFERENCES "group" ("id");