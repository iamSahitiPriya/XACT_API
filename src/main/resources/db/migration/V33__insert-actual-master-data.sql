INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (8, 'Talent Development');

INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (23,'Talent Development', 8);

---------------------------------------------------------------------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (75, 'Learning Strategy & Business Planning', 23);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES (187,'Learning Strategy & Business Planning',75 );

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (579, 'Does the learning team have a differentiated skilling strategy exclusively focused on digital talent?', 187);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (580, 'How adequate is the reserved budget for digital capability building, as part of the overall learning budget? ', 187);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (581, 'How strongly have key learning outcomes, impacted business goals / metrics?', 187);


INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (75, 'TWO',
        'Low level of maturity. Happens in pockets, or plays out in an ad-hoc manner. Visible as an "executer"');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (75, 'THREE',
        'Evolving level of maturity. Acts as a support system and enabler of relevant goals');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (75, 'FOUR',
        'Adequate maturity and already has a few best practices/successes in the scope of digital talent skilling.
Acts as a collaborator with the business');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (75, 'FIVE',
        'Highly effective and differentiated skilling strategies for digital talent. Helps business achieve differentiation and sustain/build competitive advantage');

---------------------------------------------------------------------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (76,'Solutions',23);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES (188,'Solutions',76 );

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (582, 'How would you rate the effectiveness of processes and mechanisms, that allow for the transfer of learning in the form of enhanced performance/productivity?', 188);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (583, 'How impactful have digital learning solutions been, in delivering on key business outcome measures?', 188);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (584, 'How adequately have digital learning solution outcomes, impacted build/buy/borrow ratios for a BU/org at large?', 188);



INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (76, 'TWO',
        'Low level of maturity. Happens in pockets, or plays out in an ad-hoc manner. Visible as an "executor"');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (76, 'THREE',
        'Evolving level of maturity. Acts as a support system and enabler of relevant goals');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (76, 'FOUR',
        'Adequate maturity and already has a few best practices/successes in the scope of digital talent skilling.
Acts as a collaborator with the business.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (76, 'FIVE',
        'Highly effective and differentiated skilling strategies for digital talent. Helps business achieve differentiation and sustain/build competitive advantage');

---------------------------------------------------------------------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (77,'Audience Needs',23);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES (189,'Audience Needs',77 );

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (585, 'How effective are current models/frameworks (if any), that are used to identify skilling needs for digital talent?', 189);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (586, 'How adequate is the reserved budget for digital capability building, as part of the overall learning budget? ', 189);

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (77, 'TWO',
        'Low level of maturity. Happens in pockets, or plays out in an ad-hoc manner. Visible as an "executer"');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (77, 'THREE',
        'Evolving level of maturity. Acts as a support system and enabler to relevant goals');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (77, 'FOUR',
        'Adequate maturity and already has a few best practices / successes in the scope of digital talent skilling.
Acts as a collaborator with the business');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (77, 'FIVE',
        'Highly effective and differentiated skilling strategies for digital talent. Helps business achieve differentiation and sustain / build competitive advantage');

---------------------------------------------------------------------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (78,'Disciplines',23);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES (190,'Disciplines',78 );

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (587, 'How would you rate the split between in-house SME-developed content versus off-the-shelf content for digital skilling?', 190);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (588, 'How effective are these learning communities, in enabling peer and social learning, across individuals and teams in the organization?', 190);

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (78, 'TWO',
        'Low level of maturity. Happens in pockets, or plays out in an ad-hoc manner. Visible as an "executer"');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (78, 'THREE',
        'Evolving level of maturity. Acts as a support system and enabler to relevant goals');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (78, 'FOUR',
        'Adequate maturity and already has a few best practices / successes in the scope of digital talent skilling.
Acts as a collaborator with the business');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (78, 'FIVE',
        'Highly effective and differentiated skilling strategies for digital talent. Helps business achieve differentiation and sustain / build competitive advantage');

---------------------------------------------------------------------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (79,'Tools & Tech',23);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES (191,'Tools & Tech',79 );

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (589, 'Please rate the ease with which skilling/learning data can be accessed by all relevant stakeholders in the organization', 191);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (590, 'How effectively have you been able to demonstrate the efficacy of various digital skilling tools/platforms etc.?', 191);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (591, 'How effective are these tools in helping you map the "state of digital talent" in real time?', 191);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (592, 'What is the frequency with which content is refreshed to mirror the current state / "in flavour" subjects for digital skilling?', 191);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (593, 'How effective have these tools and platforms been, in shaping career paths, and contributing to build/buy / borrow strategies for a business unit?', 191);

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (79, 'TWO',
        'Low level of maturity. Happens in pockets, or plays out in an ad-hoc manner. Visible as an "executer"');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (79, 'THREE',
        'Evolving level of maturity. Acts as a support system and enabler to relevant goals');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (79, 'FOUR',
        'Adequate maturity and already has a few best practices / successes in the scope of digital talent skilling.
Acts as a collaborator with the business');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (79, 'FIVE',
        'Highly effective and differentiated skilling strategies for digital talent. Helps business achieve differentiation and sustain / build competitive advantage');

---------------------------------------------------------------------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (80,'Learning Culture',23);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 192,'Learning Culture',80);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (594, 'How effective has the learning organization been in promoting a culture of continuous learning to stay relevant in the digital world?', 192);

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (80, 'TWO',
        'Low level of maturity. Happens in pockets, or plays out in an ad-hoc manner. Visible as an "executer"');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (80, 'THREE',
        'Evolving level of maturity. Acts as a support system and enabler to relevant goals');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (80, 'FOUR',
        'Adequate maturity and already has a few best practices / successes in the scope of digital talent skilling.
Acts as a collaborator with the business');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (80, 'FIVE',
        'Highly effective and differentiated skilling strategies for digital talent. Helps business achieve differentiation and sustain / build competitive advantage');


SELECT setval('category_seq',(select max(category_id)+1 from tbm_assessment_category),false);
SELECT setval('module_seq',(select max(module_id)+1 from tbm_assessment_module),false);
SELECT setval('topic_seq',(select max(topic_id)+1 from tbm_assessment_topic),false);
SELECT setval('parameter_seq',(select max(parameter_id)+1 from tbm_assessment_parameter),false);
SELECT setval('question_seq',(select max(question_id)+1 from tbm_assessment_question),false);
