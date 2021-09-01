DROP VIEW IF EXISTS approved_vacation_v;

CREATE OR REPLACE VIEW approved_vacation_v AS (
    select
        vr.id as id,
        vr.id as vacation_id,
        vr.creator_id as user_id,
        vr.date_from as date_start,
        vr.date_to as date_end,
        true as approved
    from vacation_request vr
    WHERE (NOT EXISTS (SELECT FROM vacation_approver WHERE request_id = vr.id AND is_approved = false)
    AND vr.is_draft = false)
);