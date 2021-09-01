create sequence hibernate_sequence start 1 increment 1;

DROP TABLE IF EXISTS "user" CASCADE ;
DROP TABLE IF EXISTS "contact" CASCADE ;
DROP TABLE IF EXISTS "skill" CASCADE ;
DROP TABLE IF EXISTS "rate_description" CASCADE ;
DROP TABLE IF EXISTS "user_Skill" CASCADE ;
DROP TABLE IF EXISTS "task" CASCADE ;


CREATE TABLE "user" (
    "id" int8 NOT NULL,
    "date_of_end_vacation" DATE NOT NULL,
    "name" varchar(255) NOT NULL,
    "surname" varchar(255) NOT NULL,
    "patronymic" varchar(255) NOT NULL,
    "position_code" smallint NOT NULL,
    "role_code" smallint NOT NULL,
    CONSTRAINT "user_pk" PRIMARY KEY ("id")
);

CREATE TABLE "contact" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL UNIQUE REFERENCES "user" ("id"),
    "email" varchar(255) NOT NULL UNIQUE,
    "phone_number" varchar(255) NOT NULL UNIQUE,
    CONSTRAINT "contact_pk" PRIMARY KEY ("id")
);

CREATE TABLE "skill" (
    "id" int8 NOT NULL,
    "name" varchar(255) NOT NULL UNIQUE,
    "description" varchar(255) NOT NULL,
    CONSTRAINT "skill_pk" PRIMARY KEY ("id")
);

CREATE TABLE "rate_description" (
    "id" int8 NOT NULL,
    "rate_number" integer NOT NULL CHECK (rate_number > 0 and rate_number < 6),
    "skill_id" int8 NOT NULL,
    "description" varchar(255) NOT NULL,
    CONSTRAINT "rate_description_pk" PRIMARY KEY ("id"),
    UNIQUE (rate_number, skill_id),
    FOREIGN KEY (skill_id) REFERENCES "skill" ("id")
);

CREATE TABLE "user_skill" (
    "id" int8 NOT NULL,

    "rate" integer NOT NULL,
    "user_id" int8 NOT NULL REFERENCES "user" ("id"),
    "skill_id" int8 NOT NULL REFERENCES "skill" ("id"),

    "is_confirmed" BOOLEAN NOT NULL,
    CONSTRAINT "user_skill_pk" PRIMARY KEY ("id"),
    UNIQUE (user_id, skill_id)
);

CREATE TABLE "task" (
    "id" int8 NOT NULL,
    "user_id" integer NOT NULL REFERENCES "user" ("id"),
    "description" varchar(255) NOT NULL,
    "is_complete" BOOLEAN,
    "set_complete_date" DATE,
    "user_setter_id" integer NOT NULL REFERENCES "user" ("id"),
    "create_date" DATE NOT NULL,
    CONSTRAINT "task_pk" PRIMARY KEY ("id"),
    CHECK (set_complete_date >= create_date)
);
