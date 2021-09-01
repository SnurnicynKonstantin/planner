DROP TABLE IF EXISTS vacation_request CASCADE;
DROP TABLE IF EXISTS vacation_approver CASCADE;

CREATE TABLE vacation_request
(
    "id" int8 NOT NULL PRIMARY KEY,
    "date_from" DATE NOT NULL,
    "date_to" DATE NOT NULL,
    "creator_id" int8 NOT NULL REFERENCES "user" (id),
    "is_draft" BOOLEAN NOT NULL,
    CHECK ("date_to" > "date_from")
);

CREATE TABLE vacation_approver
(
    "id" int8 NOT NULL PRIMARY KEY,
    "request_id" int8 NOT NULL REFERENCES "vacation_request" (id),
    "approver_id" int8 NOT NULL REFERENCES "user" (id),
    "is_approved" BOOLEAN NOT NULL default false
);