
CREATE DATABASE [IF NOT EXISTS] synch_db;

CREATE SCHEMA [IF NOT EXISTS] staging;

-- Navigate to elrr area 
SET search_path = staging;

CREATE TABLE staging."import" (
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

CREATE SEQUENCE staging.import_seq
   START WITH 1
   INCREMENT BY 50
   NO MINVALUE
   NO MAXVALUE
   CACHE 1;

ALTER SEQUENCE staging.import_seq OWNED BY staging.import.importid;



CREATE TABLE staging.importdetail (
	importdetailid int4 NOT NULL,
	importid int4 NULL,
	importbegintime timestamp NULL,
	importendtime timestamp NULL,
	totalrecords int4 NULL,
	successrecords int4 NULL,
	failedrecords int4 NULL,
	recordstatus varchar NULL,
	CONSTRAINT importdetail_pk PRIMARY KEY (importdetailid)
);

-- staging.importdetail foreign keys
ALTER TABLE staging.importdetail ADD CONSTRAINT importdetail_fk FOREIGN KEY (importid) REFERENCES staging."import"(importid);

CREATE SEQUENCE staging.importdetail_seq
   START WITH 1
   INCREMENT BY 50
   NO MINVALUE
   NO MAXVALUE
   CACHE 1;

ALTER SEQUENCE staging.importdetail_seq OWNED BY staging.importdetail.importdetailid;



CREATE TABLE staging.syncrecord (
	inserteddate timestamp NULL,
	updatedby varchar NULL,
	lastmodified timestamp NULL,
	syncrecordid int4 NOT NULL,
	importdetailid int4 NULL,
	synckey varchar NULL,
	recordstatus varchar NULL,
	CONSTRAINT syncrecord_pk PRIMARY KEY (syncrecordid)
);

-- staging.syncrecord foreign keys
ALTER TABLE staging.syncrecord ADD CONSTRAINT syncrecord_fk FOREIGN KEY (importdetailid) REFERENCES staging.importdetail(importdetailid);

CREATE SEQUENCE staging.syncrecord_seq
   START WITH 1
   INCREMENT BY 50
   NO MINVALUE
   NO MAXVALUE
   CACHE 1;

ALTER SEQUENCE staging.syncrecord_seq OWNED BY staging.syncrecord.syncrecordid;



CREATE TABLE staging.syncrecorddetail (
	syncrecorddetailid int4 NOT NULL,
	syncrecordid int4 NULL,
	"jsonb" varchar NULL,
	recordstatus varchar NULL,
	inserteddate timestamp NULL,
	updatedby varchar NULL,
	lastmodified timestamp NULL,
	payload varchar NULL,
	learner varchar NULL,
	CONSTRAINT syncrecorddetail_pk PRIMARY KEY (syncrecorddetailid)
);

-- staging.syncrecorddetail foreign keys
ALTER TABLE staging.syncrecorddetail ADD CONSTRAINT syncrecorddetail_fk FOREIGN KEY (syncrecordid) REFERENCES staging.syncrecord(syncrecordid);

CREATE SEQUENCE staging.syncrecorddetail_seq
   START WITH 1
   INCREMENT BY 50
   NO MINVALUE
   NO MAXVALUE
   CACHE 1;

   ALTER SEQUENCE staging.syncrecorddetail_seq OWNED BY staging.syncrecorddetail.syncrecorddetailid;