ALTER TABLE "task"
    ADD COLUMN IF NOT EXISTS "actual" BOOLEAN NOT NULL default false;