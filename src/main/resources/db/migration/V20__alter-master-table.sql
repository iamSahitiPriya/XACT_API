SELECT setval('category_seq',(select max(category_id)+1 from tbm_assessment_category),false);
SELECT setval('module_seq',(select max(module_id)+1 from tbm_assessment_module),false);
SELECT setval('topic_seq',(select max(topic_id)+1 from tbm_assessment_topic),false);
SELECT setval('parameter_seq',(select max(parameter_id)+1 from tbm_assessment_parameter),false);
SELECT setval('question_seq',(select max(question_id)+1 from tbm_assessment_question),false);

UPDATE tbm_assessment_module SET category=7 WHERE module_id = 7;
DELETE FROM tbm_assessment_category where category_id = 3;
UPDATE tbm_assessment_module SET is_active = false where module_id=7;