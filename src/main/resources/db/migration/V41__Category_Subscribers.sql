DROP TABLE IF EXISTS category_subscriber;

CREATE TABLE category_subscriber (
    "id" int8 NOT NULL PRIMARY KEY,
    "request_category" int8 NOT NULL REFERENCES "request_category" ("id") default 1,
    "subscriber" int8 NOT NULL REFERENCES "user" ("id")
);
