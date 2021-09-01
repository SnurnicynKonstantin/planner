DROP TABLE IF EXISTS announcement CASCADE;

CREATE TABLE announcement
(
    "id" int8 NOT NULL PRIMARY KEY,
    "creator_id" int8 NOT NULL REFERENCES "user" ("id"),
    "text" VARCHAR(255) NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE role
    ADD COLUMN IF NOT EXISTS announcement_operations boolean DEFAULT FALSE;

UPDATE role
SET announcement_operations = TRUE
WHERE name = 'ADMIN';

UPDATE role
SET announcement_operations = FALSE
WHERE name = 'USER';