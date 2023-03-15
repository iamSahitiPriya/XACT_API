create table tbl_contributors_data (
                                question_id SERIAL PRIMARY KEY,
                                question_text text NOT NULL,
                                parameter int references tbm_assessment_parameter(parameter_id),
                                question_status varchar(20) CHECK(question_status='Sent_For_Review' OR question_status='Approved' OR question_status='Rejected' OR question_status='Requested_For_Change' OR question_status='Idle') DEFAULT 'Idle',
                                author_comments text ,
                                reviewer_comments text ,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE tbm_assessment_module ADD COLUMN author varchar(200) ;