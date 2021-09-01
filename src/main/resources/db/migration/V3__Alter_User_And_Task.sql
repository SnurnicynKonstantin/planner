ALTER TABLE "user" ADD "date_of_start_vacation" DATE;
ALTER TABLE "task" ADD "data_change" DATE;
ALTER TABLE "task" ADD "user_change_id" INTEGER REFERENCES "user" ("id");