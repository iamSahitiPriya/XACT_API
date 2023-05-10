/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

alter table tbm_assessment_topic add  column  hasReference bool;
alter table tbm_assessment_parameter add column hasReference bool;


update tbm_assessment_topic as topic set hasReference = true where topic.topic_id =(select distinct(topic) from tbm_assessment_topic_reference where topic=topic.topic_id);
update tbm_assessment_topic as topic set hasReference = false where topic.hasReference is null;
update tbm_assessment_parameter as parameter set hasReference = true where parameter.parameter_id =(select distinct(parameter) from tbm_assessment_param_reference where parameter=parameter.parameter_id);
update tbm_assessment_parameter as parameter set hasReference = false where parameter.hasReference is null;




