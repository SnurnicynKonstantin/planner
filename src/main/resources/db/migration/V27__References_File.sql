ALTER TABLE "file"
    ADD CONSTRAINT "file_pk" PRIMARY KEY ("id");

ALTER TABLE "user_file"
    ADD CONSTRAINT "user_file_pk" foreign key ("user_id") references "user" ("id");

ALTER TABLE "user_file"
    ADD CONSTRAINT "file_user_pk" foreign key ("file_id") references "file" ("id");

ALTER TABLE "user_file"
    DROP CONSTRAINT IF EXISTS "user_file_unique";

ALTER TABLE "user_file"
    ADD CONSTRAINT "user_file_unique" UNIQUE ("user_id", "file_id");