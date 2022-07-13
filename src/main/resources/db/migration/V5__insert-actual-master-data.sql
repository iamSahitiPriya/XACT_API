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

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 54, 'ONE', 'Observability patterns such as log aggregation, distributed tracing, health checks, alerting, APM, etc. are not used');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 54, 'TWO', 'Observability patterns such as log aggregation, distributed tracing, health checks, alerting, APM, etc. are used sporadically by individual teams' );
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 54, 'THREE', 'Observability patterns such as log aggregation, distributed tracing, health checks, alerting, APM, etc. are documented, with varying implementations' );
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 54, 'FOUR', 'Implementation of Observability patterns such as log aggregation, distributed tracing, health checks, alerting, APM, etc. are standardized.' );
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 54, 'FIVE', 'Observability patterns such as log aggregation, distributed tracing, health checks, alerting, APM, etc. are tested to meet SLAs' );

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 55, 'ONE', 'Active passive with MTTR of 24 hours or more');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 55, 'TWO', 'Active passive with MTTR between 12 and 24 hours');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 55, 'THREE', 'Active passive with MTTR between 6 and 12 hours');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 55, 'FOUR', 'Active passive with MTTR between 2 to 6 hours');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 55, 'FIVE', 'Active active with zero downtime');


INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 56, 'ONE', 'Manually provisioned, long-running bare metal servers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 56, 'TWO', 'Manually provisioned, long-running virtual machine servers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 56, 'THREE', 'Programmatically provisioned, long running, mutable virtual machine servers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 56, 'FOUR', 'Programmatically provisioned, ephemeral, immutable virtual machine servers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 56, 'FIVE', 'Programmatically provisioned, ephemeral, immutable containers or serverless functions.');


INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 57, 'ONE', 'Application configuration is embedded within application deployment artifacts for a fixed set of environments. New environment(s) or changes to application configuration require an application rebuild. Feature toggles are not used to turn on/off application functionality dynamically. Secrets are embedded within application deployment artifacts and are managed manually.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 57, 'TWO', 'Application configuration is embedded within application artifacts. Environment specific configuration parameters can be overridden without requiring an application rebuild. However, overrides are not audited. Changes to configuration require an application redeployment. Feature toggles are embedded within application deployment artifacts. Changes to toggles require an application rebuild. Secrets are externalized from application deployment artifacts, but are managed manually.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 57, 'THREE', 'Application configuration is externalizable from application artifacts. But there is no operationalized mechanism to do so. Teams continue to require rebuilds when a change to application configuration is required. Feature toggles are externalized from application deployment artifacts. Individual teams use custom methods to implement toggle functionality. Turning off functionality reliably requires intimate knowledge of application internals. Secrets are externalized from application deployment artifacts, with encrypted secrets being stored in source control.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 57, 'FOUR', 'Application configuration is externalized from the deployable application artifact. Teams making use of externalized configuration use custom (including homegrown) methods to do so. Feature toggles are externalized from application deployment artifacts. Teams use a consistent approach to toggles, but toggling off functionality still requires knowledge of application internals. Secrets are externalized from application deployment artifacts, with encrypted secrets being stored in a configuration management system.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 57, 'FIVE', 'Application configuration is externalized from the deployable application artifact. All teams use a consistent method to make changes to application configuration. All changes are traceable to a source code check-in. Feature toggles are externalized from application deployment artifacts. Toggles are defined at a business feature level. Secrets are externalized from application deployment artifacts, with encrypted secrets being stored in a secrets management server.');
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
VALUES ( 47,'Does API validate file size, format etc?', 5);

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 22,'How are APIs documented?', 6);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 23,'How are cross-cutting concerns such as authentication, rate limiting etc. implemented?', 6);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 24,'Are there any APM tools to monitor APIs?', 6);

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 25,'What is server side Tech?', 7);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 26,'What is client side Tech?', 7);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 27,'What is cloud Tech?', 7);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (28,'What are supporting tools / libraries?',7);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (29,'What is the database / data storage?',7);



INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 30,'Is Chaos Engineering practiced to test the resilience of the system?', 8);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 31,'Is the component resilience approach as simple as it needs to be, but no simpler?', 8);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 32,'Does the component separate downstream dependency health from it''s own health', 8);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (33,'Is the component tolerant of both upstream and downstream component failure?',8);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (34,'Does the component/system support active/active cross-site for resilience and failover?',8);


INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 35,'Is there any performance test suite?', 9);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 36,'What is the scaling approach? Horizotal / vertical?', 9);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 37,'How is cache-bursting handled in case of static assets?', 9);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (38,'Is there any CDN being used?',9);

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 39,'Are SLOs published and agreed upon by business stakeholders?', 10);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 40,'Are througputs and response times continuously measured?', 10);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (41,'Are alerts configured in case of a departure from SLOs?',10);

INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (42,'Do Architecture Decision Records exist?',11);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (43,'How are architecture decisions made?',11);


INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (44,'Does the documented architecture diagram depict the reality?',12);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (45,'How and when are architecture reviews done?',12);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (46,'Are fitness functions used? To what degree are they automated?',12);

INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (48,'Is CPU and Memory usage monitored?',54);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (49,'Are critical errors monitored?',54);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (50,'Any notification or alerts for monitoring?',54);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (51,'Infra-health monitoring?',54);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (52,'Service-health monitoring?',54);


INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (53,'How is incident management done?',55);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (54,'What strategy is followed for DB recovery?',55);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (55,'What is max and min time taken for disaster recovery on production so far?',55);

INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (56,'What are the different environments setup? In which environment does the QA perform story testing?',56);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (57,'How are environments provisioned? What strategy is used to make sure they are provisioned consistently?',56);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (58,'Does the environment automatically scale to accommodate higher load?',56);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (59,'How stable and predictable are environments? Are environments isolated from one another with respect to deployed components, databases, etc.?',56);


INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (60,'How is application configuration managed?',57);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (61,'Is there any strategy in place to reduce the cost? (Eg: during times of less user activity)',57);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (62,'How is application functionality turned on/off dynamically?',57);
INSERT INTO tbm_assessment_question(question_id,question_text,parameter)
VALUES (63,'How are secrets managed?',57);