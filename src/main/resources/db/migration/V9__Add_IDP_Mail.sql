ALTER TABLE "user" ADD "idp_date" DATE;

CREATE TABLE "mail_idp" (
    "id" int8 NOT NULL,
    "IDP_date" DATE NOT NULL,
    "user_id" int8 NOT NULL REFERENCES "user" ("id"),
    CONSTRAINT "mail_pk" PRIMARY KEY ("id")
);