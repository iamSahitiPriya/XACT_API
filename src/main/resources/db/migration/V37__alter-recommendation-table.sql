UPDATE tbl_assessment_topic_recommendation set delivery_horizon = 'LATER' where delivery_horizon is null or delivery_horizon not in ('NOW','NEXT','LATER');
UPDATE tbl_assessment_parameter_recommendation set delivery_horizon = 'LATER' where delivery_horizon is null or delivery_horizon not in ('NOW','NEXT','LATER');
UPDATE tbl_assessment_topic_recommendation set impact = 'LOW' where impact is null;
UPDATE tbl_assessment_parameter_recommendation set impact = 'LOW' where impact is null;
UPDATE tbl_assessment_topic_recommendation set effort = 'LOW' where effort is null;
UPDATE tbl_assessment_parameter_recommendation set effort = 'LOW' where effort is null;

ALTER table tbl_assessment_topic_recommendation alter column delivery_horizon type varchar(50);
Alter table tbl_assessment_topic_recommendation add constraint deliveryHorizon check ( delivery_horizon='NOW' OR delivery_horizon='NEXT' OR delivery_horizon='LATER');
alter table tbl_assessment_topic_recommendation alter column delivery_horizon set not null;
alter table tbl_assessment_topic_recommendation alter column impact set not null;
alter table tbl_assessment_topic_recommendation alter column effort set not null;

ALTER table tbl_assessment_parameter_recommendation alter column delivery_horizon type varchar(50);
Alter table tbl_assessment_parameter_recommendation add constraint deliveryHorizon check ( delivery_horizon='NOW'OR delivery_horizon='NEXT'OR delivery_horizon='LATER' );
alter table tbl_assessment_parameter_recommendation alter column delivery_horizon set not null;
alter table tbl_assessment_parameter_recommendation alter column impact set not null;
alter table tbl_assessment_parameter_recommendation alter column effort set not null;
