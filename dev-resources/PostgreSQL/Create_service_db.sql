/*
 * Name: ELRR Prototype ELRR Create DDL
 * Date: 2021.08.06
 */

CREATE DATABASE [IF NOT EXISTS] service_db;

CREATE SCHEMA [IF NOT EXISTS] elrr;

-- Navigate to elrr area 
SET search_path = elrr;

-- Create elrr tables 
CREATE TABLE IF NOT EXISTS CONFIGURATION (
	configurationid int8 NOT NULL,
	configurationname varchar(255) NOT NULL,
	configurationvalue varchar(255) NULL,
	frequency varchar(255) NULL,
	starttime varchar(255) NULL,
    primarycontact varchar(255) NULL,
	primaryemail varchar(254) NULL,
	primaryorgname varchar(100) NULL,
	primaryphone varchar(255) NULL,
	secondarycontact varchar(255) NULL,
	secondaryemail varchar(254) NULL,
	secondaryorgname varchar(100) NULL,
	secondaryphone varchar(255) NULL,
    recordstatus varchar(10) NULL,
    updatedby varchar(20) NULL,
    inserteddate timestamp NULL,
    lastmodified timestamp NULL,
	CONSTRAINT configuration_pk PRIMARY KEY (configurationid)
);


CREATE TABLE IF NOT EXISTS COMPETENCY (
	competencyid int4 NOT NULL,
	competencydefinitionidentifier varchar(100) NULL,
	competencydefinitionidentifierurl text NULL,
	competencytaxonomyid varchar(100) NULL,
	competencydefinitionvalidstartdate date NULL,
	competencydefinitionvalideenddate date NULL,
	competencydefinitionparentidentifier varchar(100) NULL,
	competencydefinitionparenturl text NULL,
	competencydescriptionparentcode varchar(100) NULL,
	competencydefinitioncode varchar(100) NULL,
	competencydefinitiontype varchar(100) NULL,
	competencydefinitiontypeurl text NULL,
	competencydefinitionstatement text NULL,
    competencyframeworktitle varchar(100) NOT NULL,
	competencyframeworkversion varchar(100) NULL,
	competencyframeworkidentifier varchar(100) NULL,
	competencyframeworkdescription text NULL,
	competencyframeworksubject varchar(100) NULL,
	competencyframeworkvalidstartdate date NULL,
	competencyframeworkvalidenddate date NULL,
    recordstatus varchar(10) NULL,
    updatedby varchar(20) NULL,
    inserteddate timestamp NULL,
    lastmodified timestamp NULL,
	CONSTRAINT competency_pk15 PRIMARY KEY (competencyid)
);

 
CREATE TABLE IF NOT EXISTS COURSE (
	courseid int4 NOT NULL,
	coursetitle varchar(300) NOT NULL,
	coursesubjectmatter varchar(100) NULL,
	coursesubjectabbreviation varchar(20) NULL,
	courseidentifier varchar(50) NOT NULL,
	courselevel varchar(50) NULL,
	coursenumber varchar(50) NULL,
	courseinstructionmethod varchar(50) NULL,
	coursestartdate date NULL,
	courseenddate date NULL,
	courseenrollmentdate date NULL,
	courseacademicgrade varchar(50) NULL,
	courseprovidername varchar(100) NULL,
	departmentname varchar(100) NULL,
	coursegradescalecode varchar(50) NULL,
	coursemetadatarepository varchar(50) NULL,
	courselrsendpoint varchar(50) NULL,
	coursedescription text NULL,
	recordstatus varchar(10) NULL,
	updatedby varchar(20) NULL,
	inserteddate timestamp NULL,
	lastmodified timestamp NULL,
	CONSTRAINT course_pk10 PRIMARY KEY (courseid)
);


CREATE TABLE IF NOT EXISTS ELRRAUDITLOG (
	elrrauditlogid int8 NOT NULL,
    payload jsonb NULL,
	syncid int8 NOT NULL,
    recordstatus varchar(10) NULL, 
    updatedby varchar(20) NULL,
    inserteddate timestamp NULL,
	lastmodified timestamp NULL,
	CONSTRAINT elrrauditlog_pk PRIMARY KEY (elrrauditlogid)
);


CREATE TABLE IF NOT EXISTS EMPLOYMENT (
	employmentid int4 NOT NULL,
	employername varchar(100) NOT NULL,
	employerdepartment varchar(100) NULL,
	hiredate date NULL,
	employmentstartdate date NULL,
	employmentenddate date NULL,
	joblevel varchar(100) NULL,
	occupation varchar(100) NULL,
	employed bpchar(1) NULL,
	primarycareercategory varchar(50) NULL,
    recordstatus varchar(10) NULL,
	updatedby varchar(20) NULL,
	inserteddate timestamp NULL,
	lastmodified timestamp NULL,
    CONSTRAINT employment_pk8 PRIMARY KEY (employmentid)
);


CREATE TABLE IF NOT EXISTS PERSON (
	personid int4 NOT NULL,
	name varchar(250) NOT NULL,
	firstname varchar(50) NOT NULL,
	middlename varchar(50) NULL,
	lastname varchar(50) NOT NULL,
	nameprefix varchar(50) NULL,
	titleaffixcode varchar(50) NULL,
	namesuffix varchar(50) NULL,
	qualificationaffixcode varchar(50) NULL,
	maidenname varchar(50) NULL,
	preferredname varchar(50) NULL,
	humanresourceidentifier text NULL,
	personnelidentificationsystem text NULL,
	birthdate date NULL,
	sex bpchar(1) NULL,
	primarylanguage varchar(50) NULL,
	militaryveteranindicator bpchar(1) NULL,
    recordstatus varchar(10) NULL,
	updatedby varchar(20) NULL,
	inserteddate timestamp NULL,
	lastmodified timestamp NULL,
	CONSTRAINT person_pk4 PRIMARY KEY (personid)
);


CREATE TABLE IF NOT EXISTS CONTACTINFORMATION (
	contactinformationid int4 NOT NULL,
	personid int4 NOT NULL,
	contactinformation varchar(20) NOT NULL,
	telephonenumber varchar(20) NULL,
	isprimaryindicator bpchar(1) NULL,
	telephonetype varchar(20) NULL,
	electronicmailaddress varchar(254) NULL,
	electronicmailaddresstype varchar(20) NULL,
	emergencycontact varchar(250) NULL,
	recordstatus varchar(10) NULL,
	updatedby varchar(20) NULL,
	inserteddate timestamp NULL,
	lastmodified timestamp NULL,
	CONSTRAINT contactinformation_pk5 PRIMARY KEY (contactinformationid)
);

ALTER TABLE CONTACTINFORMATION ADD CONSTRAINT refperson20 FOREIGN KEY (personid) REFERENCES person(personid);


-- Re-visit relationships
CREATE TABLE IF NOT EXISTS LEARNERPROFILE (
	learnerprofileid int4 NOT NULL,
	personid int4 NOT NULL,
	learneraddressid int4 NULL,
	contactinformationid int4 NULL,
	employmentid int4 NOT NULL,
	positionid int4 NULL,
	citizenshipid int4 NULL,
	studentid int4 NULL,
	courseid int4 NULL,
	courseaccreditationid int4 NULL,
	competencyid int4 NULL,
	credentialid int4 NULL,
	organizationid int4 NULL,
	organizationaddressid int4 NULL,
	accreditationid int4 NULL,
	activitystatus varchar(10) NOT NULL,
	recordstatus varchar(10) NULL,
	updatedby varchar(20) NULL,
	inserteddate timestamp NULL,
	lastmodified timestamp NULL,
    CONSTRAINT learnerprofile_pk PRIMARY KEY (learnerprofileid)
);

ALTER TABLE learnerprofile ADD CONSTRAINT refemployment25 FOREIGN KEY (employmentid) REFERENCES employment(employmentid);
ALTER TABLE person ADD CONSTRAINT ref_person FOREIGN KEY (personid) REFERENCES person(personid); 


CREATE TABLE IF NOT EXISTS ORGANIZATION (
	organizationid int4 NOT NULL,
	organizationname varchar(100) NOT NULL,
	organizationidentifier varchar(100) NULL,
	organizationidentificationcode varchar(100) NULL,
	organizationidentificationsystem varchar(100) NULL,
	industrytypeidentifier varchar(100) NULL,
	organizationfein varchar(100) NULL,
	organizationdescription text NULL,
	parentorganization varchar(100) NULL,
	recordstatus varchar(10) NULL,
	updatedby varchar(20) NULL,
	inserteddate timestamp NULL,
	lastmodified timestamp NULL,
	CONSTRAINT organization_pk9 PRIMARY KEY (organizationid)
);


CREATE TABLE IF NOT EXISTS ROLE (
	roleid int4 NOT NULL,
	rolename varchar(20) NULL,
	recordstatus varchar(10) NULL,
	updatedby varchar(20) NULL,
	inserteddate date NULL,
	lastmodified date NULL,
	CONSTRAINT role_pk16 PRIMARY KEY (roleid)
);


--Re-visit role/person relationships => view
CREATE TABLE IF NOT EXISTS ROLERELATIONS (
    rolerelationsid int4 NOT NULL,
	parentroleid int4 NOT NULL,
	parentpersonid int4 NOT NULL,
	childroleid int4 NOT NULL,
	childpersonid int4 NOT NULL,
    recordstatus varchar(10) NULL,
    updatedby varchar(20) NULL,
    inserteddate date NULL,
    lastmodified date NULL,
    CONSTRAINT rolerelations_pk PRIMARY KEY (rolerelationsid)
);
