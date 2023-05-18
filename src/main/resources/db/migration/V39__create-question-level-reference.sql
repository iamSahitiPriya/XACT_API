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
