create table tbm_assessment_category (
                                  category_id INT PRIMARY KEY,
                                  category_name varchar(500) NOT NULL

);
create table tbm_assessment_module (
                               module_id INT PRIMARY KEY,
                               module_name varchar(500) NOT NULL,
                               category int references tbm_assessment_category(category_id)

);
create table tbm_assessment_topic (
                               topic_id INT PRIMARY KEY,
                               topic_name varchar(500) NOT NULL,
                               module int references tbm_assessment_module(module_id),
                               assessment_level varchar(20) CHECK(assessment_level='Topic' OR assessment_level='Parameter')

);
create table tbm_assessment_parameter (
                               parameter_id INT PRIMARY KEY,
                               parameter_name varchar(500) NOT NULL,
                               topic int references tbm_assessment_topic(topic_id)
);

create table tbm_assessment_question (
                               question_id INT PRIMARY KEY,
                               question_text text NOT NULL,
                               parameter int references tbm_assessment_parameter(parameter_id)
);

create table tbm_assessment_param_reference (
                               reference_id SERIAL PRIMARY KEY,
                               parameter int references tbm_assessment_parameter(parameter_id),
                               rating varchar(50),
                               reference text
);

create table tbm_assessment_topic_reference (
                               reference_id SERIAL PRIMARY KEY,
                               topic int references tbm_assessment_topic(topic_id),
                               rating varchar(50),
                               reference text
);


create table tbl_assessment_topic (
                               assessment_id int references tbl_assessment(assessment_id),
                               topic_id  int references tbm_assessment_topic(topic_id),
                               score int ,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               primary key (assessment_id,topic_id)
);

create table tbl_assessment_parameter (
                               assessment_id int references tbl_assessment(assessment_id),
                               parameter_id  int references tbm_assessment_parameter(parameter_id),
                               score int ,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               primary key (assessment_id,parameter_id)
);

create table tbl_assessment_question (
                                          assessment_id int references tbl_assessment(assessment_id),
                                          question_id int references tbm_assessment_question(question_id),
                                          notes text,
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          primary key (assessment_id,question_id)
);
