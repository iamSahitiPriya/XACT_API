create table tbl_user_assessment_question (
                question_id SERIAL PRIMARY KEY ,
                assessment int references tbl_assessment(assessment_id),
                parameter  int references tbm_assessment_parameter(parameter_id),
                question_text text NOT NULL ,
                answer_text text,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);