-- Create schema
CREATE schema IF NOT EXISTS datasync_schema;

-- Navigate to datasync_schema schema
SET search_path = datasync_schema;

-- Drop datasync_schema tables
DROP TABLE IF EXISTS datasync_schema."import" CASCADE;
DROP TABLE IF EXISTS datasync_schema.elrrauditlog CASCADE;

CREATE TABLE IF NOT EXISTS datasync_schema."import" (
    id UUID         PRIMARY KEY,
    importname      varchar NULL,
    importstartdate timestamp NULL,
    importenddate   timestamp NULL,
    recordstatus    varchar NULL,
    retries         int4    NULL,
    updatedby       varchar NULL,
    inserteddate    timestamp NULL,
    lastmodified    timestamp NULL
);

CREATE TABLE IF NOT EXISTS datasync_schema.elrrauditlog (
    id UUID         PRIMARY KEY,
    statementid     varchar(50) NULL,
    updatedby       varchar(20) NULL,
    inserteddate    timestamp NULL,
    lastmodified    timestamp NULL
);