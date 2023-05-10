/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

alter table tbm_assessment_topic add  column  has_reference bool default false;
alter table tbm_assessment_parameter add column has_reference bool default  false;


update tbm_assessment_topic as topic set has_reference = true where topic.topic_id =(select distinct(topic) from tbm_assessment_topic_reference where topic=topic.topic_id);
update tbm_assessment_parameter as parameter set has_reference = true where parameter.parameter_id =(select distinct(parameter) from tbm_assessment_param_reference where parameter=parameter.parameter_id);




