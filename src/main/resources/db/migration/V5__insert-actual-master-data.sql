INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 52, 'ONE', 'Check-ins happen on a long running private branch and remain there until story/feature completion. Integration to the mainline usually takes several weeks or months. The build process does not include adequate testing. Builds are stable, but usually require a significant amount of manual testing to certify quality. Failed builds remain red for prolonged periods of time. Check-ins on top of failed builds are fairly common. There is no clear ownership assignment for failed builds.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 52, 'TWO', 'Check-ins initially happen on a private branch and remain there for the duration of a development sprint/iteration. Integration to the mainline happens every 2 to 4 weeks. The build process does include various types of automated testing. But these tests are time-consuming and flaky. Hence they require a significant amount of manual testing to certify quality. Failed builds remain red for prolonged periods of time. Usually someone takes ownership, but fixing red builds takes a long time.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 52, 'THREE', 'Check-ins and builds happen on a private branch. Features are usually fine grained to finish within a few days. Integration happens every few days. The build process includes a good mix of unit, integration, functional and acceptance tests, and these tests are mostly green. However,  a significant amount of manual testing is still needed to certify quality - primarily because of poor visibility into the quality of the tests that run as part of the build. Failed builds for prolonged periods of time are inevitable. Developers are unable to run stages in the pipeline locally and there isn''t enough debugging information readily available.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 52, 'FOUR', 'Check-ins and CI happen on a short-lived private branch to satisfy a pull-request workflow. A branch-by-abstraction style is used to turn off incomplete work. Integration is truly continuous. The build process includes a good mix of unit, integration, functional and acceptance tests, and these tests are mostly green. Additional manual testing is required for scenarios not covered through automated testing. Failed builds are not that common. But when failures do happen, other team members wait for the build to be fixed. Team members avoid checking in on top of a red build.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 52, 'FIVE', 'Check-ins and CI happen on the mainline. A branch-by-abstraction style is used to turn off incomplete work. Integration is truly continuous. The build process includes a good mix of unit, integration, contract, functional and acceptance tests, and these tests are mostly green. Manual testing only used for exploratory testing. Failed builds are not that common. When they do occur, team members are empowered to rollback the cause of the build failure if they are not fixed by the owner.');

------------------------------------------------------------------

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 7,'How are checkins done and what branching strategy is used?', 52);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 8,'How much of testing is included in the build process - unit, integration, functional, acceptance, contract?', 52);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 9,'How are failed builds handled and how long do they typically remain red?', 52);


INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 53, 'ONE', 'EAR, WAR style into a shared application container with multiple applications/services running in the same process space. Manual with downtime and outage window. Monthly or even less frequently. Deployments can only happen during low traffic "windows", because there is downtime when switching to the new version. There is always an extended, pre-emptive deployment freeze during peak periods (e.g. Thanksgiving, Christmas etc.) - no negotiations - except in case of critical bug fixes or');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 53, 'TWO', 'Mutable deployment target with configuration management software e.g. puppet, chef, etc. Automated with downtime with outage window Frequency - Weekly or more. Deployments can only happen during low traffic "windows", because of a poor track record with failed (customer impacting) deployments. There is always an extended, pre-emptive deployment freeze during peak periods, but deployments are possible with proper justifications.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 53, 'THREE', 'Immutable, versioned virtual machine(VM) image e.g. VMDK, AMI etc. Automated with zero downtime - blue green. Frequency - Almost Daily. Deployments can happen anytime during the day, but continue to happen only during low traffic "windows" due to inertia. There are short deployment freeze during peak periods, but these are merely a formality. Deployments usually are done as and when necessary.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 53, 'FOUR', 'Immutable, versioned container image e.g. docker, rocket etc. Automated with zero downtime - rolling or canary. Frequency - Daily. Deployments can happen anytime during the day, but continue to happen only during specific times of the day due to a lack of approvers, deployment operations personnel etc. There are no deployment freezes at any time of the year. However, deployment frequency slows during peak periods due to a lack of availability of key personnel (business and operations).');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 53, 'FIVE', 'Versioned compound container image groups e.g. helm, docker-compose etc. Automated with zero downtime - combination of blue-green and rolling. Frequency - On-demand, several times a day. Any time functionality is ready and the business wants to release it. Deployments can and happen anytime during the day. The business/product stakeholder is in complete  control of when deployments should happen. Teams are free to deploy whenever they wish as long as the product/business team members are comfortable. Deployment is not a function of IT constraints, simply a business decision.');


INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 10,'How are artifacts packaged?', 53);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 11,'How are deployments done - manual/semi-automated/automated? How much of downtime is incurred?', 53);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 12,'How frequently are deployments done?', 53);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 13,'Are there deployment windows and moratoriums like deployment freezes?', 53);


-----------------------------

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 14,'Does API follow standards like RESTful?', 4);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 15,'Is there a style guide for APIs?', 4);

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 16,'How is API versioning done?', 5);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 17,'Does API support file upload?', 5);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 18,'Does API validate the request params?', 5);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 19,'What are the data format supports?', 5);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 20,'Is API protected?', 5);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 21,'Is API response data restricted?', 5);

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 22,'How are APIs documented?', 6);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 23,'How are cross-cutting concerns such as authentication, rate limiting etc. implemented?', 6);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 24,'Are there any APM tools to monitor APIs?', 6);







