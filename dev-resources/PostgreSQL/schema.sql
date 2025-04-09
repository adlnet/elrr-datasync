-- Create schema
CREATE schema IF NOT EXISTS staging;

-- Navigate to staging schema
SET search_path = staging;

-- Drop staging tables
DROP TABLE IF EXISTS staging.importdetail cascade;
DROP TABLE IF EXISTS staging."import" cascade;
DROP TABLE IF EXISTS staging.syncrecorddetail cascade;
DROP TABLE IF EXISTS staging.syncrecord cascade;
DROP TABLE IF EXISTS staging.errors cascade;
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



CREATE TABLE IF NOT EXISTS staging.importdetail (
    importdetailid int4 NOT NULL,
    importid int4 NULL,
    importbegintime timestamp NULL,
    importendtime timestamp NULL,
    totalrecords int4 NULL,
    successrecords int4 NULL,
    failedrecords int4 NULL,
    recordstatus varchar NULL,
    CONSTRAINT importdetail_pk PRIMARY KEY (importdetailid),
    CONSTRAINT importdetail_fk FOREIGN KEY (importid) REFERENCES staging."import"(importid)
);

CREATE SEQUENCE IF NOT EXISTS staging.importdetail_seq
   START WITH 1
   INCREMENT BY 1
   NO MINVALUE
   NO MAXVALUE
   CACHE 1
   NO CYCLE;

ALTER SEQUENCE staging.importdetail_seq OWNED BY staging.importdetail.importdetailid;



CREATE TABLE IF NOT EXISTS staging.syncrecord (
    inserteddate timestamp NULL,
    updatedby varchar NULL,
    lastmodified timestamp NULL,
    syncrecordid int4 NOT NULL,
    importdetailid int4 NULL,
    synckey varchar NULL,
    recordstatus varchar NULL,
    CONSTRAINT syncrecord_pk PRIMARY KEY (syncrecordid),
    CONSTRAINT syncrecord_fk FOREIGN KEY (importdetailid) REFERENCES staging.importdetail(importdetailid)
);

CREATE SEQUENCE IF NOT EXISTS staging.syncrecord_seq
   START WITH 1
   INCREMENT BY 1
   NO MINVALUE
   NO MAXVALUE
   CACHE 1
   NO CYCLE;

ALTER SEQUENCE staging.syncrecord_seq OWNED BY staging.syncrecord.syncrecordid;



CREATE TABLE IF NOT EXISTS staging.syncrecorddetail (
    syncrecorddetailid int4 NOT NULL,
    syncrecordid int4 NULL,
    recordstatus varchar NULL,
    inserteddate timestamp NULL,
    updatedby varchar NULL,
    lastmodified timestamp NULL,
    CONSTRAINT syncrecorddetail_pk PRIMARY KEY (syncrecorddetailid),
    CONSTRAINT syncrecorddetail_fk FOREIGN KEY (syncrecordid) REFERENCES staging.syncrecord(syncrecordid)
);

CREATE SEQUENCE IF NOT EXISTS staging.syncrecorddetail_seq
   START WITH 1
   INCREMENT BY 1
   NO MINVALUE
   NO MAXVALUE
   CACHE 1
   NO CYCLE;

ALTER SEQUENCE staging.syncrecorddetail_seq OWNED BY staging.syncrecorddetail.syncrecorddetailid;
   
   
   
CREATE TABLE IF NOT EXISTS staging.elrrauditlog (
    elrrauditlogid int8 NOT NULL,
    syncid int8 NOT NULL,
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