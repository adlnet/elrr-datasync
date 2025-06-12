-- Create schema
CREATE schema IF NOT EXISTS staging;

-- Navigate to staging schema
SET search_path = staging;

-- Drop staging tables
DROP TABLE IF EXISTS staging."import" CASCADE;
DROP TABLE IF EXISTS staging.elrrauditlog CASCADE;

CREATE TABLE IF NOT EXISTS staging."import" (
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

CREATE TABLE IF NOT EXISTS staging.elrrauditlog (
    id UUID         PRIMARY KEY,
    statementid     varchar(50) NULL,
    updatedby       varchar(20) NULL,
    inserteddate    timestamp NULL,
    lastmodified    timestamp NULL
);