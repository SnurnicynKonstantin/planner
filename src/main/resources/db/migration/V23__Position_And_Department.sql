DROP TABLE IF EXISTS "department" CASCADE;
DROP TABLE IF EXISTS "position" CASCADE;

CREATE TABLE "department"
(
    "id" int8 NOT NULL PRIMARY KEY,
    "name" varchar(255) NOT NULL UNIQUE,
    "description" varchar(255)
);

CREATE TABLE "position"
(
    "id" int8 NOT NULL PRIMARY KEY,
    "name" varchar(255) NOT NULL UNIQUE,
    "description" varchar(255)
);

insert into position(id, name, description)
    values
           (1, 'UNDEFINED', 'Не определен'),
           (2, 'JUNIOR', 'Младший разработчик'),
           (3, 'MIDDLE', 'Разработчик'),
           (4, 'SENIOR', 'Старший разработчик');

insert into department(id, name, description)
    VALUES
            (1, 'DEVELOPMENT', 'Отдел разработки'),
            (2, 'TESTING', 'Отдел тестирования'),
            (3, 'ANALYTICS', 'Отдел аналитики'),
            (4, 'UNDEFINED', 'Не определен');

ALTER TABLE "user"
    ALTER COLUMN position_code TYPE int8;

ALTER TABLE "user"
    RENAME COLUMN "position_code" TO "position_id";

ALTER TABLE "user"
    ALTER COLUMN department_code TYPE int8;

ALTER TABLE "user"
    RENAME COLUMN  "department_code" TO "department_id";

alter table "user"
    drop constraint if exists user_position_fk;

alter table "user"
    add constraint user_position_fk foreign key (position_id) references "position" (id);

alter table "user"
    drop constraint if exists user_department_fk;

alter table "user"
    add constraint user_department_fk foreign key (department_id) references "department" (id);