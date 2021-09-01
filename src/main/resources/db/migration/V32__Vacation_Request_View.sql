DROP VIEW IF EXISTS vacation_request_v;

CREATE OR REPLACE VIEW vacation_request_v AS (
    select
     vr.id as id,
     bool_and(va.is_approved) as is_approved,
     vr.date_from as date_from
    from "vacation_request" vr
          left join "vacation_approver" va on va.request_id = vr.id
    group by (vr.id, vr.date_from)
);

CREATE TABLE "vacation_mail" (
    "id" int8 NOT NULL PRIMARY KEY,
    "vacation_request_id" int8 NOT NULL REFERENCES "vacation_request" (id),
    "vacation_user_id" int8 NOT NULL REFERENCES "user" (id),
    UNIQUE ("vacation_request_id", "vacation_user_id")
)