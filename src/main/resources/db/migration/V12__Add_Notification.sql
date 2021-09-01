DROP TABLE IF EXISTS notification CASCADE;

CREATE TABLE notification
(
    "id" int8 NOT NULL,
    "recipient_id" int8 NOT NULL REFERENCES "user" ("id"),
    "text" varchar(255) NOT NULL,
    "creation_date" DATE,
    "is_checked" BOOLEAN DEFAULT FALSE,
    CONSTRAINT notification_pk PRIMARY KEY ("id")
);