INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (19, 'Code Quality', 1);

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (48, 'Readability', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (49, 'Maintainability', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (50, 'Re-usability', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (51, 'Evolvability', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (52, 'Complexity', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (53, 'Testability', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (54, 'Tech Debt', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (55, 'Logging', 19);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (56, 'Exception Handling', 19);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 132,'Coding standards', 48);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 133,'Design patterns & practices', 49);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 134,'Code sanity & Code quality', 49);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 135,'Code coupling & integration ', 50);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 136,'Design patterns', 51);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 137,'Code complexity & risk', 52);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 138,'Test strategy', 53);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 139,'Tracking & absorption', 54);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 140,'Logging basic hygiene', 55);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 141,'Logging best practices', 55);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 142,'Exception handling practices', 56);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (316, 'Is proper naming convention being followed for constants/classes/methods?', 132);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (317, 'Is there any styleguide?', 132);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (318, 'Is code modular?', 132);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (319, 'Is each layer independent? ex : Service should have its own contract and Data Objects should have its own contract. ', 133);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (320, 'Are functions/methods following Single responsibility principle?', 133);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (321, 'How are configurations managed? Are those specific to clients / environments?', 133);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (322, 'Is proper Authentication/ Authorization happening for all endpoints?', 134);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (323, 'Is there any dead code?', 134);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (324, 'Are there any code smells? (like Long parameter list, Magic numbers etc)', 134);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (325, 'Are unnecessary data/parameters being passed?', 134);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (326, 'Any linter used?', 134);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (327, 'How are different features being enabled / disabled for each client / environment?', 134);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (328, 'How components are reused in integration module for various different format?', 135);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (329, 'How much change is required to integrate with a new tool?', 135);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (330, 'Is any reusable UI components?', 135);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (331, 'Is SOLID principle followed?', 136);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (332, 'In case of localization, Is there a common localization framework available across application?', 136);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (333, 'Is there any patterns used? Eg: MVC', 136);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (334, 'Cognitive complexity of the applications', 137);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (335, 'Is there any complex algorithm involved?', 137);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (336, 'Can small change break unrelated things due to complexity of understanding', 137);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (337, 'Are there any unit tests?', 138);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (338, 'Are components are loosely coupled?', 138);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (339, 'Can external API be mocked?', 138);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (340, 'Is the code is inherently testable?', 138);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (341, 'Are there any tech debt captured?', 139);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (342, 'How much percentage spent on every release?', 139);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (343, 'Is there a logging framework available and proper logging  pattern being followed?', 140);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (344, 'Are logging levels consistent across the application?', 140);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (345, 'Is logging format consistent across the application?', 141);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (346, 'Any secrets or personal information being logged?', 141);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (347, 'What is the default Logging level? (Error)', 141);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (348, 'Are logs being overused?', 141);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (349, 'Can logs be traced end-to-end across modules/components ?', 141);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (350, 'Is proper exceptional handling followed across application?', 142);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (351, 'Are unchecked/runtime exceptions being handled? These exceptions need to handled as part of the code. Application should check only for checked exceptions', 142);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (352, 'Are exceptions being propagated? Throw early Catch Late', 142);

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 48, 'ONE', 'No coding standard followed, Code is difficult to convey functional meaning.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 48, 'THREE', 'Partly following coding standards, code is modular but all other standards are not in place.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 48, 'FIVE', 'following almost all coding standards, code is modular and follow strict style guide and naming conventions. methods/classes represent function/responsibility efficiently.');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 49, 'ONE', 'No clear separation of responsibility and naming does not reflect the responsibility, No linting and no code quality measures exist');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 49, 'TWO', 'Very little code follows SRP but naming convention does not reflect the responsibility, Code quality is measured manually sometimes');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 49, 'THREE', 'Portion of the code base follow SRP but naming does not allow easy identification of the responsibility, Code quality is analysed and measured manually.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 49, 'FOUR', 'Code follows SRP and most of the code easily understandable, Automated ways to analyse and measure code quality.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 49, 'FIVE', 'Single Responsibility Principle has been followed and naming allows for easy identification of responsibility, Automated analysis of code quality in place, prerequisite measures like linting,static scan etc.');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 50, 'ONE', 'Components are tightly coupled, Support only specific formats. Very difficult to onboard new integration/consumer.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 50, 'THREE', 'Components are coupled, Support some formats and integration protocols. Fairly difficult to onboard new integration/consumer.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 50, 'FIVE', 'Components are loosely coupled, Support most of formats and integration protocols. Fairly easy to onboard new integration/consumer.');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 51, 'ONE', 'No Design Patterns being used. Design does not reflect domain. Functional changes spill over to unrelated areas of code.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 51, 'TWO', 'Very few design patterns being used. Design does not reflect domain. Functional changes may spill to unrelated areas of code.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 51, 'THREE', 'Design patterns are being followed in some part of the code but design is not driven by domain');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 51, 'FOUR', 'Design patterns are being followed in most of the code. Some part of design also reflects domain. But for other parts functional changes may spill in unrelated areas');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 51, 'FIVE', 'Most of the code uses Design Patterns and design is driven by domain. Functional changes are easy and limited to related classes/files');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 52, 'ONE', 'Code is difficult to understand, difficult to change. Changes carry high risk of breaking something');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 52, 'TWO', 'Very little code is easy to understand or change. Changes carry significant risk of breaking something');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 52, 'THREE', 'Some part of the code is easy to understand and change without breaking elsewhere. But there are other parts that are neither easy to understand nor easy to change without breaking something');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 52, 'FOUR', 'Most of the code is self-explanatory, easy to change. Changes sometimes break unrelated parts of other code');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 52, 'FIVE', 'Self-explanatory code, easy to change. Changes do not carry risk of breaking unrelated code');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 53, 'ONE', 'No automated tests and automating tests are not possible');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 53, 'TWO', 'Most of the code is not testable and there are very few tests');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 53, 'THREE', 'There are some tests and some of the code can be inherently testable');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 53, 'FOUR', 'Most of the code is testable but there are few tests');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 53, 'FIVE', 'There is healthy automated tests and code inherently testable');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 54, 'ONE', 'Tech debt is not tracked at all');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 54, 'TWO', 'Tech debt is not tracked but arbitrarily some tech debt is paid down');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 54, 'THREE', 'Only some part of known Tech debt is tracked and prioritised but not regularly');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 54, 'FOUR', 'All tech debt is tracked. But not prioritised and worked upon regularly');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 54, 'FIVE', 'All known tech debt is tracked and prioritised regularly based on value/effort, with equal priority as other functional features');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 55, 'ONE', 'Logs are not available at all. Any tracing is almost impossible.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 55, 'TWO', 'Logging framework is in place but logs are not consistent in application. Format and log levels are also not consistent. Tracing is very difficult.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 55, 'THREE', 'Logging framework is in place but logs are not consistent in application. Format is defined but log levels are also not consistent. Some times log is overused and create large log files. Tracing is fairly complex.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 55, 'FOUR', 'Logging framework is in place and logs are consistent in application. Format is defined and log levels are also consistent. Sometime PII gets logged in, sometimes logs are overused, End to end tracing is also not possible.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 55, 'FIVE', 'Logging framework is in place and logs are consistent in application. Format is defined and log levels are also consistent. End to end tracing is easily available and can be easily understandable.');

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 56, 'ONE', 'No exception handling.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 56, 'THREE', 'Some of exception handled but application still can raise and propagate exception which goes back to consumer/logs without handing.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 56, 'FIVE', 'Exception handling is implemented, checked exception are propagated and handled via throw early catch late principle, unchecked exception are handled at top level layer to avoid unintentionally revelation of code/logic/stacktrace. ');

SELECT setval('category_seq',(select max(category_id)+1 from tbm_assessment_category),false);
SELECT setval('module_seq',(select max(module_id)+1 from tbm_assessment_module),false);
SELECT setval('topic_seq',(select max(topic_id)+1 from tbm_assessment_topic),false);
SELECT setval('parameter_seq',(select max(parameter_id)+1 from tbm_assessment_parameter),false);
SELECT setval('question_seq',(select max(question_id)+1 from tbm_assessment_question),false);