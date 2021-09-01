ALTER TABLE "history"
    ADD COLUMN IF NOT EXISTS comment varchar(255);
