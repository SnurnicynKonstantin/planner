DROP VIEW IF EXISTS user_skill_v;

CREATE OR REPLACE VIEW user_skill_v AS (
    select
        us.id as id,
        u.id as user_id,
        u.login as user_login,
        s."name" as skill_name,
        us.rate as skill_rate,
        us.version as version,
        us.skill_id as skill_id
    from "user" u
        INNER JOIN (
            SELECT id, user_id, rate, version, skill_id, deleted
            FROM user_skill
            WHERE (user_id, version, skill_id) IN (
                SELECT user_id, MAX(version) as version, skill_id
                FROM user_skill
                GROUP BY user_id, skill_id
            )
        ) us ON us.user_id = u.id
            INNER JOIN skill s ON s.id = us.skill_id
        WHERE us.deleted = false
);