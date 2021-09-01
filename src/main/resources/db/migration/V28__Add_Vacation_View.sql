DROP VIEW IF EXISTS vacation_v;

CREATE OR REPLACE VIEW vacation_v AS (
    select
        va.id as id,
        vr.id as vacation_id,
        vr.creator_id as user_id,
        vr.date_from as date_start,
        vr.date_to as date_end,
        va.is_approved as approved
    from vacation_request vr
            LEFT JOIN vacation_approver va ON va.request_id = vr.id
    WHERE vr.is_draft = false
);