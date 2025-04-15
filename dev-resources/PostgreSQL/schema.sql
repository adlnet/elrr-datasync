-- Create schema
CREATE schema IF NOT EXISTS staging;

-- Navigate to staging schema
SET search_path = staging;

-- Drop staging tables
DROP TABLE IF EXISTS staging."import" cascade;
DROP TABLE IF EXISTS staging.elrrauditlog cascade;

CREATE TABLE IF NOT EXISTS staging."import" (
    importid int4 NOT NULL,
    importname varchar NULL,
    importstartdate timestamp NULL,
    importenddate timestamp NULL,
    recordstatus varchar NULL,
    updatedby varchar NULL,
    inserteddate timestamp NULL,
    lastmodified timestamp NULL,
    CONSTRAINT import_pk PRIMARY KEY (importid)
);

CREATE SEQUENCE IF NOT EXISTS staging.import_seq
   START WITH 1
   INCREMENT BY 1
   NO MINVALUE
   NO MAXVALUE
   CACHE 1
   NO CYCLE;

ALTER SEQUENCE staging.import_seq OWNED BY staging.import.importid;


CREATE TABLE IF NOT EXISTS staging.elrrauditlog (
    elrrauditlogid int8 NOT NULL,
    statement varchar(2048) NULL,
    updatedby varchar(20) NULL,
    inserteddate timestamp NULL,
    lastmodified timestamp NULL,
    CONSTRAINT elrrauditlog_pk PRIMARY KEY (elrrauditlogid)
);

CREATE SEQUENCE IF NOT EXISTS staging.elrrauditlog_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    NO CYCLE;

ALTER SEQUENCE staging.elrrauditlog_seq OWNED BY staging.elrrauditlog.elrrauditlogid;