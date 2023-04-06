create table tbl_module_contributors (
                                module int references tbm_assessment_module(module_id),
                                user_email varchar(50) NOT NULL ,
                                role varchar(50) check ( role='AUTHOR' or role='REVIEWER') NOT NULL ,
                                primary key (module,user_email)
);

Alter table tbm_assessment_question add column
    question_status varchar(20)  DEFAULT 'PUBLISHED' CHECK(question_status='SENT_FOR_REVIEW' OR question_status='REJECTED' OR question_status='REQUESTED_FOR_CHANGE' OR question_status='DRAFT' OR question_status='PUBLISHED'),
    add column  comments text ,
    add column created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    add column updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;


ALTER TABLE tbl_user_assessment_question ADD COLUMN contribution_status boolean default false;

