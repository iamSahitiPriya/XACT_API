create table tbl_organisation (
                                  organisation_id SERIAL PRIMARY KEY,
                                  organisation_name varchar(50) NOT NULL,
                                  industry varchar(50) NOT NULL,
                                  domain varchar(50) NOT NULL,
                                  size int NOT NULL
);
create table tbl_assessment (
                               assessment_id SERIAL PRIMARY KEY,
                               assessment_name varchar(50) NOT NULL,
                               organisation int references tbl_organisation(organisation_id),
                               assessment_status varchar(20) CHECK(assessment_status='Active' OR assessment_status='Completed'),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table tbl_assessment_users (
                                     assessment_id int references tbl_assessment(assessment_id),
                                     user_email varchar(50) NOT NULL , 
                                     role varchar(50) CHECK(role='Facilitator' OR role='Developer' OR role='Owner'),
                                     primary key (assessment_id,user_email)
);


