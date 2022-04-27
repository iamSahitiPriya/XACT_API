create table tbm_assessment_category (
                                  category_id SERIAL PRIMARY KEY,
                                  category_name varchar(50) NOT NULL
);
create table tbm_assessment_module (
                               module_id SERIAL PRIMARY KEY,
                               module_name varchar(50) NOT NULL,
                               category int references tbm_assessment_category(category_id)
);
create table tbm_assessment_topic (
                               topic_id SERIAL PRIMARY KEY,
                               topic_name varchar(50) NOT NULL,
                               module int references tbm_assessment_module(module_id)
);
create table tbm_assessment_parameter (
                               parameter_id SERIAL PRIMARY KEY,
                               parameter_name varchar(50) NOT NULL,
                               topic int references tbm_assessment_topic(topic_id)
);

create table tbm_assessment_question (
                               question_id SERIAL PRIMARY KEY,
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
                               score double precision,
                               notes text,
                               recommendation text,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table tbl_assessment_parameter (
                               assessment_id int references tbl_assessment(assessment_id),
                               parameter_id  int references tbm_assessment_parameter(parameter_id),
                               score double precision,
                               notes text,
                               recommendation text,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
