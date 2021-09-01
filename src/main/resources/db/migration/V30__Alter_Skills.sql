DELETE FROM "user_skill";
DELETE FROM "rate_description";
DELETE FROM "skill";
DELETE FROM "group";

ALTER TABLE "skill" ALTER COLUMN description DROP NOT NULL;