ALTER TABLE "user"
ADD COLUMN IF NOT EXISTS department_code smallint DEFAULT 4;