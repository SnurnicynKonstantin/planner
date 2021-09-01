ALTER TABLE "task"
    ADD COLUMN IF NOT EXISTS parent_id int8;
