DROP TABLE IF EXISTS project CASCADE;

CREATE TABLE project
(
    "id" int8 NOT NULL,
    "name" VARCHAR(255) NOT NULL unique,
    "description" VARCHAR(255),
    "start_date" DATE,
    "end_date" DATE,
    CONSTRAINT project_pk PRIMARY KEY ("id"),
    CHECK (end_date >= start_date)
);

ALTER TABLE history ADD COLUMN IF NOT EXISTS project_id int8;
alter table history drop constraint if exists project_history_fk;
alter table history add constraint project_history_fk foreign key (project_id) references project (id);
