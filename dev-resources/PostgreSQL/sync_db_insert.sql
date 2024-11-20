SET search_path TO staging;


-- Turncate tables
TRUNCATE staging.importdetail cascade;
TRUNCATE staging."import" cascade;
TRUNCATE staging.syncrecorddetail cascade;
TRUNCATE staging.syncrecord cascade;
COMMIT;



-- Restart sequences
ALTER SEQUENCE staging.import_importid_seq RESTART;
ALTER SEQUENCE staging.importdetail_importdetailid_seq RESTART;
ALTER SEQUENCE staging.syncrecord_syncrecordid_seq RESTART;
ALTER SEQUENCE staging.syncrecorddetail_syncrecorddetailid_seq RESTART;
COMMIT;



-- Insert data
INSERT INTO staging."import" (recordstatus , importname, importstartdate, importenddate)
VALUES ('SUCCESS', 'Deloitte LRS', '2000-12-30 13:08:54.193', '2000-12-30 13:08:54.193');
COMMIT;



INSERT INTO staging.importdetail (recordstatus, importid, importbegintime, importendtime, totalrecords, successrecords, failedrecords)
VALUES ('SUCCESS', 1, '2000-12-30 13:08:54.193', '2000-12-30 13:08:54.193',1,1,0);
COMMIT;



INSERT INTO staging.syncrecord (recordstatus , synckey, importdetailid)
VALUES ('inserted', 'Deloitte LRS',1 );
COMMIT;



INSERT INTO staging.syncrecorddetail (syncrecordid, recordstatus)
VALUES (1, 'inserted');
COMMIT;

UPDATE staging.syncrecorddetail set learner = '{"contactEmailAddress":"test@deloitte.com","name":"test","courses":[{"courseId":"5","courseName":"coursename","userCourseStatus":"inserted"}]}'
 WHERE syncrecorddetailid  = 1;
COMMIT;