ALTER TABLE tbm_assessment_category ADD CONSTRAINT category_name_unq UNIQUE (category_name);
ALTER TABLE tbm_assessment_module ADD CONSTRAINT module_name_unq UNIQUE (module_name, category);
ALTER TABLE tbm_assessment_topic ADD CONSTRAINT topic_name_unq UNIQUE (topic_name, module);
ALTER TABLE tbm_assessment_parameter ADD CONSTRAINT parameter_name_unq UNIQUE (parameter_name, topic);




