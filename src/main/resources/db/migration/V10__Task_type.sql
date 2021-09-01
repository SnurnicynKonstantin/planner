ALTER TABLE "task"
    ADD COLUMN IF NOT EXISTS task_type_code integer NOT NULL;

ALTER TABLE "task"
    ADD COLUMN IF NOT EXISTS title varchar(255) NOT NULL;

ALTER TABLE "task"
    ALTER COLUMN description TYPE varchar(255);

ALTER TABLE "task"
    ADD COLUMN IF NOT EXISTS counter integer DEFAULT 1;

ALTER TABLE "task"
    ADD CHECK ( counter >= 1 )