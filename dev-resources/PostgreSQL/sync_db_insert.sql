SET search_path TO staging;


TRUNCATE staging.importdetail cascade;
TRUNCATE staging."import" cascade;
TRUNCATE staging.syncrecorddetail cascade;
TRUNCATE staging.syncrecord cascade;
COMMIT;



-- RESET sequences
ALTER SEQUENCE staging.import_seq RESTART;
ALTER SEQUENCE staging.importdetail_seq RESTART;
ALTER SEQUENCE staging.syncrecord_seq RESTART;
ALTER SEQUENCE staging.syncrecorddetail_seq RESTART;
COMMIT;



INSERT INTO staging."import" (importid , recordstatus , importname, importstartdate, importenddate)
VALUES (1, 'SUCCESS', 'Deloitte LRS', '2000-12-30 13:08:54.193', '2000-12-30 13:08:54.193');
COMMIT;

SELECT setval('staging.import_seq', 1, true);



INSERT INTO staging.importdetail  (importdetailid , recordstatus, importid, importbegintime, importendtime, totalrecords, successrecords, failedrecords)
VALUES (1, 'SUCCESS', 1, '2000-12-30 13:08:54.193', '2000-12-30 13:08:54.193',1,1,0);
COMMIT;

SELECT setval('staging.importdetail_seq', 1, true);



INSERT INTO staging.syncrecord  (syncrecordid , recordstatus , synckey, importdetailid)
VALUES (1, 'inserted', 'Deloitte LRS',1 );
COMMIT;

SELECT setval('staging.syncrecord_seq', 1, true);



INSERT INTO staging.syncrecorddetail  (syncrecorddetailid, syncrecordid, recordstatus)
VALUES (1, 1, 'inserted');
COMMIT;

SELECT setval('staging.syncrecorddetail_seq', 1, true);

update staging.syncrecorddetail set learner = '{"contactEmailAddress":"test@deloitte.com","name":"test","courses":[{"courseId":"5","courseName":"coursename","userCourseStatus":"inserted"}]}'
 where syncrecorddetailid  = 1;
COMMIT;