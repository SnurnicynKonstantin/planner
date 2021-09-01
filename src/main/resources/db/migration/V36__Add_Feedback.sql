DROP TABLE IF EXISTS "feedback" CASCADE;

CREATE TABLE "feedback" (
    "id" int8 NOT NULL,
    "creator_id" int8,
    "creation_date" DATE NOT NULL,
    "text" TEXT NOT NULL,
    "is_archived" BOOLEAN NOT NULL,
    CONSTRAINT "feedback_pk" PRIMARY KEY ("id")
);