create table tbl_organisation (
                                  organisation_id int NOT NULL ,
                                  organisation_name varchar(50) NOT NULL,
                                  industry varchar(50) NOT NULL,
                                  domain varchar(50) NOT NULL,
                                  size int NOT NULL,
                                  primary key (organisation_id)
);
create table tbl_assessment (
                               assessment_id int primary key,
                               assessment_name varchar(50) NOT NULL,
                               organisation int references tbl_organisation(organisation_id),
                               description varchar(250) NOT NULL,
                               assessment_status char(20) CHECK(assessment_status='ACTIVE' OR assessment_status='INACTIVE'),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table tbl_assessment_users (
                                     assessment_id int references tbl_assessment(assessment_id),
                                     user_email varchar(50) NOT NULL ,
                                     first_name varchar(50) NOT NULL,
                                     last_name varchar(50) NOT NULL,
                                     role varchar(50) CHECK(role='Facilitator' OR role='Developer' OR role='Owner'),
                                     primary key (assessment_id,user_email)
);


