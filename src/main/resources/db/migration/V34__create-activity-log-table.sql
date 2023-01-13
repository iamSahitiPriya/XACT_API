create table tbl_activity_log (
    assessment int references tbl_assessment(assessment_id),
    id int,
    topic int references tbm_assessment_topic(topic_id),
    activity_type varchar(50) not null,
    user_name varchar(100) not null,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (assessment,user_name)
)