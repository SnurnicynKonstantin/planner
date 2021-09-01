DROP TABLE IF EXISTS file CASCADE;
DROP TABLE IF EXISTS user_file CASCADE;

CREATE TABLE file
(
    "id" int8 NOT NULL,
    "upload_date" DATE NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "blob" bytea NOT NULL,
    "file_type_code" smallint DEFAULT 3,
    "comment" VARCHAR(255),
    "uploader_id" int8 NOT NULL,
    UNIQUE (name, uploader_id)
);

CREATE TABLE user_file
(
    "id" serial NOT NULL,
    "user_id" int8 NOT NULL,
    "file_id" int8 NOT NULL
);