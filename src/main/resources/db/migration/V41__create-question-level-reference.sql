/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

create table tbm_assessment_question_reference (
                                                   reference_id SERIAL PRIMARY KEY,
                                                   question int references tbm_assessment_question(question_id),
                                                   rating varchar(50),
                                                   reference text
);
alter table tbl_assessment_question add column score int;
alter table tbm_assessment_question_reference drop constraint tbm_assessment_question_reference_question_fkey;

alter table tbm_assessment_question_reference add constraint tbm_assessment_question_reference_fkey foreign key (question) references tbm_assessment_question (question_id)
    on delete cascade
