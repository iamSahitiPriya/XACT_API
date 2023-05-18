/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

alter table tbm_assessment_topic add  column  has_reference bool default false;
alter table tbm_assessment_parameter add column has_reference bool default  false;


update tbm_assessment_topic as topic set has_reference = true where topic.topic_id in (select distinct(topic) from tbm_assessment_topic_reference);
update tbm_assessment_parameter as parameter set has_reference = true where parameter.parameter_id in (select distinct(parameter) from tbm_assessment_param_reference);

alter table tbm_assessment_question_reference drop constraint tbm_assessment_question_reference_question_fkey;

alter table tbm_assessment_question_reference add constraint tbm_assessment_question_reference_fkey foreign key (question) references tbm_assessment_question (question_id)
    on delete cascade

