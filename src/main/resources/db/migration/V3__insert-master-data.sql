INSERT INTO tbm_assessment_category(category_id, category_name)
    VALUES (1,'My Category1');

INSERT INTO tbm_assessment_module(module_id,module_name, category)
	VALUES (1, 'My Module', 1);

INSERT INTO tbm_assessment_topic(topic_id,topic_name, module)
	VALUES (1, 'My Topic', 1);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
	VALUES ( 1,'My Parameter', 1);


INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
	VALUES ( 1,'My Question', 1);


INSERT INTO tbm_assessment_topic_reference(
	topic, rating, reference)
	VALUES ( 1, 'ONE', 'Reference1');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( 1, 'TWO', 'Reference2');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( 1, 'THREE', 'Reference3');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( 1, 'FOUR', 'Reference4');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( 1, 'FIVE', 'Reference5');

INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( 1, 'ONE', 'Reference1');
INSERT INTO tbm_assessment_param_reference(
	parameter , rating, reference)
	VALUES ( 1, 'TWO', 'Reference2');
INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( 1, 'THREE', 'Reference3');
INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( 1, 'FOUR', 'Reference4');
INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( 1, 'FIVE', 'Reference5');


