ALTER TABLE "user"
    ADD COLUMN IF NOT EXISTS manager_id int8;

ALTER TABLE "user"
    ADD FOREIGN KEY (manager_id) REFERENCES "user"("id");