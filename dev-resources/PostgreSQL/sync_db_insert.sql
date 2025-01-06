-- Navigate to staging schema
SET search_path TO staging;

-- Truncate tables
TRUNCATE staging.importdetail cascade;
TRUNCATE staging."import" cascade;
TRUNCATE staging.syncrecorddetail cascade;
TRUNCATE staging.syncrecord cascade;
TRUNCATE staging.errors cascade;

-- Insert data
INSERT INTO staging."import" (importid , recordstatus , importname, importstartdate, importenddate)
VALUES (1, 'SUCCESS', 'Deloitte LRS', '2000-12-30 13:08:54.193', '2000-12-30 13:08:54.193');

SELECT setval('"staging"."import_seq"'::regclass, (SELECT MAX("importid") FROM "staging"."import"));



INSERT INTO staging.importdetail  (importdetailid , recordstatus, importid, importbegintime, importendtime, totalrecords, successrecords, failedrecords)
VALUES (1, 'SUCCESS', 1, '2000-12-30 13:08:54.193', '2000-12-30 13:08:54.193',1,1,0);

SELECT setval('"staging"."importdetail_seq"'::regclass, (SELECT MAX("importdetailid") FROM "staging"."importdetail"));



INSERT INTO staging.syncrecord  (syncrecordid , recordstatus , synckey, importdetailid, retries)
VALUES (1, 'inserted', 'Deloitte LRS', 1, 0);

SELECT setval('"staging"."syncrecord_seq"'::regclass, (SELECT MAX("syncrecordid") FROM "staging"."syncrecord"));



INSERT INTO staging.syncrecorddetail  (syncrecorddetailid, syncrecordid, recordstatus)
VALUES (1, 1, 'inserted');

SELECT setval('"staging"."syncrecorddetail_seq"'::regclass, (SELECT MAX("syncrecorddetailid") FROM "staging"."syncrecorddetail"));

UPDATE staging.syncrecorddetail SET learner = '{"contactEmailAddress":"test@deloitte.com","name":"test","courses":[{"courseId":"5","courseName":"coursename","userCourseStatus":"inserted"}]}'
 WHERE syncrecorddetailid = 1;