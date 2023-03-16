create table tbl_module_contributors (
                                module int references tbm_assessment_module(module_id),
                                user_email varchar(50) NOT NULL ,
                                role varchar(50) check ( role='Author' or role='Reviewer') NOT NULL ,
                                primary key (module,user_email)
);

Alter table tbm_assessment_question add column
    question_status varchar(20)  DEFAULT 'Published' CHECK(question_status='Sent_For_Review' OR question_status='Approved' OR question_status='Rejected' OR question_status='Requested_For_Change' OR question_status='Idle' OR question_status='Published'),
    add column  author_comments text ,
    add column reviewer_comments text ,
    add column created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    add column updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;


ALTER TABLE tbl_user_assessment_question ADD COLUMN contribution_status boolean default false;

