DROP VIEW IF EXISTS user_v;

CREATE OR REPLACE VIEW user_v AS (
    select
        u.id as id,
        u.login as user_login,
        u.name as user_name,
        u.surname as user_surname,
        u.personal_information as user_personal_information,
        r.name as role_name,
        r.id as role_id,
        p.description as position_description,
        p.id as position_id,
        d.description as department_description,
        d.id as department_id,
        v.approved as is_on_vacation
    from "user" u
            LEFT JOIN department d ON d.id = u.department_id
            LEFT JOIN "position" p on p.id = u.position_id
            LEFT JOIN "role" r on r.id = u.role_id
            LEFT JOIN "approved_vacation_v" v on v.user_id = u.id and v.approved = true and CURRENT_DATE BETWEEN v.date_start and v.date_end
);