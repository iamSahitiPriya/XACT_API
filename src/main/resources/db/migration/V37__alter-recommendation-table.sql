ALTER table tbl_assessment_topic_recommendation alter column delivery_horizon type varchar(50);
Alter table tbl_assessment_topic_recommendation add constraint deliveryHorizon check ( delivery_horizon='NOW' OR delivery_horizon='NEXT' OR delivery_horizon='LATER');
ALTER table tbl_assessment_parameter_recommendation alter column delivery_horizon type varchar(50);
Alter table tbl_assessment_parameter_recommendation add constraint deliveryHorizon check ( delivery_horizon='NOW'OR delivery_horizon='NEXT'OR delivery_horizon='LATER' );