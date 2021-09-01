ALTER TABLE "user_skill"
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL default false;