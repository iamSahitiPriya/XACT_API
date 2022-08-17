CREATE SEQUENCE category_seq OWNED BY tbm_assessment_category.category_id;
SELECT setval('category_seq',(select max(category_id)+1 from tbm_assessment_category),false);
ALTER TABLE tbm_assessment_category ALTER COLUMN category_id SET DEFAULT nextval('category_seq');

CREATE SEQUENCE module_seq OWNED BY tbm_assessment_module.module_id;
SELECT setval('module_seq',(select max(module_id)+1 from tbm_assessment_module),false);
ALTER TABLE tbm_assessment_module ALTER COLUMN module_id SET DEFAULT nextval('module_seq');

CREATE SEQUENCE topic_seq OWNED BY tbm_assessment_topic.topic_id;
SELECT setval('topic_seq',(select max(topic_id)+1 from tbm_assessment_topic),false);
ALTER TABLE tbm_assessment_topic ALTER COLUMN topic_id SET DEFAULT nextval('topic_seq');

CREATE SEQUENCE parameter_seq OWNED BY tbm_assessment_parameter.parameter_id;
SELECT setval('parameter_seq',(select max(parameter_id)+1 from tbm_assessment_parameter),false);
ALTER TABLE tbm_assessment_parameter ALTER COLUMN parameter_id SET DEFAULT nextval('parameter_seq');

CREATE SEQUENCE question_seq OWNED BY tbm_assessment_question.question_id;
SELECT setval('question_seq',(select max(question_id)+1 from tbm_assessment_question),false);
ALTER TABLE tbm_assessment_question ALTER COLUMN question_id SET DEFAULT nextval('question_seq');

ALTER TABLE tbm_assessment_parameter ADD COLUMN "is_active" BOOLEAN DEFAULT TRUE;
ALTER TABLE tbm_assessment_topic ADD COLUMN "is_active" BOOLEAN DEFAULT TRUE;

ALTER TABLE tbm_assessment_category ADD COLUMN "comments" text;
ALTER TABLE tbm_assessment_module ADD COLUMN "comments" text;
ALTER TABLE tbm_assessment_parameter ADD COLUMN "comments" text;
ALTER TABLE tbm_assessment_topic ADD COLUMN "comments" text;

ALTER TABLE tbm_assessment_category ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_module ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_parameter ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_topic ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tbm_assessment_category ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_module ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_parameter ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_topic ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

