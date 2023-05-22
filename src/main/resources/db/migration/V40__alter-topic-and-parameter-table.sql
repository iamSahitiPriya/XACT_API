/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

alter table tbm_assessment_topic add  column  has_reference bool default false;
alter table tbm_assessment_parameter add column has_reference bool default  false;


update tbm_assessment_topic as topic set has_reference = true where topic.topic_id in (select distinct(topic) from tbm_assessment_topic_reference);
update tbm_assessment_parameter as parameter set has_reference = true where parameter.parameter_id in (select distinct(parameter) from tbm_assessment_param_reference);


