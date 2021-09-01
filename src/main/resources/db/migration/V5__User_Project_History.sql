DROP TABLE IF EXISTS history CASCADE;

CREATE TABLE history
(
    "id"   int8 NOT NULL,
    "user_id" int8 NOT NULL REFERENCES "user" ("id"),
    "project_name" varchar(255) NOT NULL,
    "work_start_date" DATE NOT NULL,
    "work_end_date" DATE NOT NULL,
    CONSTRAINT history_pk PRIMARY KEY ("id"),

    CHECK (work_end_date >= work_start_date)
);
