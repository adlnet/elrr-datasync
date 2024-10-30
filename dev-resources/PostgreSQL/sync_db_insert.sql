SET search_path TO staging;

INSERT INTO staging."import" (importid , recordstatus , importname, importstartdate, importenddate)
VALUES (1, 'SUCCESS', 'Deloitte LRS', '2000-12-30 13:08:54.193', '2100-12-30 13:08:54.193');

INSERT INTO staging.importdetail  (importdetailid  , recordstatus , importid , importbegintime , importendtime, totalrecords, successrecords, failedrecords)
VALUES (1, 'SUCCESS', 1, '2000-12-30 13:08:54.193', '2100-12-30 13:08:54.193',1,1,0);


INSERT INTO staging.syncrecord  (syncrecordid , recordstatus , synckey, importdetailid)
VALUES (1, 'inserted', 'Deloitte LRS',1 );

update staging.syncrecord set recordstatus  ='inserted' where staging.syncrecordid  =1;


INSERT INTO staging.syncrecorddetail  (syncrecorddetailid ,syncrecordid , recordstatus)
VALUES (1, 1, 'inserted' );


update staging.syncrecorddetail  set learner = '{"contactEmailAddress":"test@deloitte.com","name":"test","courses":[{"courseId":"5","courseName":"coursename","userCourseStatus":"inserted"}]}'
 where staging.syncrecorddetailid  = 1;