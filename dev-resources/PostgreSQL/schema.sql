-- Create schema
CREATE schema IF NOT EXISTS datasync_schema;

-- Navigate to datasync_schema schema
SET search_path = datasync_schema;

-- Drop datasync_schema tables
DROP TABLE IF EXISTS datasync_schema."import" CASCADE;
DROP TABLE IF EXISTS datasync_schema.elrrauditlog CASCADE;

DROP TYPE IF EXISTS record_status CASCADE;

DO $$ BEGIN
    CREATE TYPE record_status AS ENUM ('SUCCESS', 'INSERTED', 'INPROCESS', 'FAILED', 'COMPLETED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS datasync_schema."import" (
    id UUID         PRIMARY KEY,
    importname      varchar NULL,
    importstartdate TIMESTAMP WITH TIME ZONE NULL,
    recordstatus    record_status,
    retries         int4    NULL,
    updatedby       varchar NULL,
    inserteddate    TIMESTAMP WITH TIME ZONE NULL,
    lastmodified    TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE IF NOT EXISTS datasync_schema.elrrauditlog (
    id UUID         PRIMARY KEY,
    statementid     varchar(50) NULL,
    updatedby       varchar(20) NULL,
    inserteddate    TIMESTAMP WITH TIME ZONE NULL,
    lastmodified    TIMESTAMP WITH TIME ZONE NULL
);