INSERT INTO tbm_assessment_category( category_name)
    VALUES ('My Category1');

INSERT INTO tbm_assessment_module(module_name, category)
	VALUES ( 'My Module', (select category_id from tbm_assessment_category c where c.category_name='My Category1'));

INSERT INTO tbm_assessment_topic(topic_name, module)
	VALUES ( 'My Topic', (select module_id from tbm_assessment_module m where m.module_name='My Module'));

INSERT INTO tbm_assessment_parameter(parameter_name, topic)
	VALUES ( 'My Parameter', (select topic_id from tbm_assessment_topic t where t.topic_name='My Topic'));


INSERT INTO tbm_assessment_question(question_text, parameter)
	VALUES ( 'My Question', (select parameter_id from tbm_assessment_parameter p where p.parameter_name='My Parameter'));


INSERT INTO tbm_assessment_topic_reference(
	topic, rating, reference)
	VALUES ( (select topic_id from tbm_assessment_topic t where t.topic_name='My Topic'), 'ONE', 'Reference1');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( (select topic_id from tbm_assessment_topic t where t.topic_name='My Topic'), 'TWO', 'Reference2');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( (select topic_id from tbm_assessment_topic t where t.topic_name='My Topic'), 'THREE', 'Reference3');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( (select topic_id from tbm_assessment_topic t where t.topic_name='My Topic'), 'FOUR', 'Reference4');
INSERT INTO tbm_assessment_topic_reference(
topic, rating, reference)
VALUES ( (select topic_id from tbm_assessment_topic t where t.topic_name='My Topic'), 'FIVE', 'Reference5');

INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( (select parameter_id from tbm_assessment_parameter p where p.parameter_name='My Parameter'), 'ONE', 'Reference1');
INSERT INTO tbm_assessment_param_reference(
	parameter , rating, reference)
	VALUES ( (select parameter_id from tbm_assessment_parameter p where p.parameter_name='My Parameter'), 'TWO', 'Reference2');
INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( (select parameter_id from tbm_assessment_parameter p where p.parameter_name='My Parameter'), 'THREE', 'Reference3');
INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( (select parameter_id from tbm_assessment_parameter p where p.parameter_name='My Parameter'), 'FOUR', 'Reference4');
INSERT INTO tbm_assessment_param_reference(
	parameter, rating, reference)
	VALUES ( (select parameter_id from tbm_assessment_parameter p where p.parameter_name='My Parameter'), 'FIVE', 'Reference5');


