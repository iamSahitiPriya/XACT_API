INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (22, 'Engineering Excellence', 1);
--------------------------------------------------------------------------------------
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module) VALUES(65,'Requirement Analysis ',22);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module) VALUES(66,'Architecture and Design',22);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module) VALUES(67,'Development',22);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module) VALUES(68,'Deployment',22);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module) VALUES(69,'Quality Assurance',22);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module) VALUES(70,'CFRs',22);

INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(164,'Requirement Analysis',65);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(165,'Requirement Design',65);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(166,'Architecture Engagement',66);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(167,'Documentation',66);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(168,'Development Discipline',67);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(169,'Developer Onboarding & Cultivation',67);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(170,'Source Control Practices',67);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(171,'Data Management Practices',67);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(172,'Pipeline',68);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(173,'Cloud Practices',68);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(174,'Shift-Left Testing/Quality',69);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(175,'Accessibility',70);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic) VALUES(176,'Logging',70);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(420,'Do you describe a set of requirements in detail using analytical techniques?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(421,'Do you ensure that a set of requirements has been developed in enough detail to be usable by a particular stakeholder, is internally consistent, and is of high quality?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(422,'Do you ensure that a set of requirements or designs delivers business value and supports the organization''s goals and objectives?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(423,'Do you structure all requirements and designs to support the overall business purpose for a change and do they work effectively as a cohesive whole?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(424,'Do you identify, explore and describe different possible ways of meeting the needs?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(425,'Do you assess the business value associated with a potential solution?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(426,'Do you compare potential solutions and different options, including trade-offs, to identify and recommend the solution option that delivers the greatest overall value?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(427,'Do you capture all the non-functional requirements?',164);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(428,'Do you describe a set of designs in detail using analytical techniques?',165);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(429,'Do you describe the approach that will be followed to transition to the future state?',165);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(430,'Do you capture the full set of requirements and their relationships for defining design options that can address the holistic set of requirements?',165);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(431,'Is architecture knowledge sharing happening?',166);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(432,'Are architectural models & diagrams maintained and centrally available for teams?',166);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(433,'Is architecture-led tech runway available for everyone for a larger purpose?',166);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(434,'Is architecture prioritized tech debt identified and picked regularly?',166);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(435,'Are Cross-Functional Requirements compliance/adoption taken care?',166);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(436,'Do you have architectural decisions captured and maintained?',167);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(437,'Is it possible for anyone to look for relevant decisions and designs from a central place?',167);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(438,'How do you keep the capabilities of each system and functioning documented and up to date?',167);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(439,'Do you follow XP practices like TDD, Pair Programming etc?',168);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(440,'Is Git a pre-commit hook for running unit tests / Linting/security code scanning?',168);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(441,'How do you maintain libraries/ dependencies up-to-date?',168);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(442,'How do the team identify tech debt and refactor it?',168);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(443,'Data for tests (performance tests, functional tests, integration) are unique and right-sized in different environments.',168);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(444,'Does the team use mocks wherever applicable?',168);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(445,'Do developers have security awareness?',169);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(446,'Does the team aware of the domain?',169);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(447,'Do developers have the same/similar setup for development to avoid local environment problems?',169);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(448,'Can new comers can setup the system in a few hours?',169);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(449,'Do developers commit early and often?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(450,'Do developers commit/merge to trunk/master? Are there short-lived feature branches?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(451,'Are most commits atomic?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(452,'Are commits traceable from user stories/tasks?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(453,'Does the commit message contain the Issue tracker # and task information?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(454,'Are configuration settings committed?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(455,'Are dependencies committed when they should be pulled as part of the build?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(456,'Do developers commit half-done work into master?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(457,'Does the team have an established workflow?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(458,'Does the team maintain versioning of deliverables/components?',170);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(459,'Application data is periodically backed up',171);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(460,'Database migration changes are captured as code (SQL, PSQL)',171);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(461,'All database changes are managed as part of the deployment pipeline.',171);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(462,'Migrations can be rolled forwards and backwards',171);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(463,'Testing periodic restores from backups',171);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(464,'Are the key metrics like No. of connections, throughput (Sequential/Index scans), tables with more disk usage, indexes, and checkpoints being monitored',171);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(465,'Do the Build Pipelines have test stages, security scanners, and static code scanners?',172);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(466,'How are artefacts created and stored? ',172);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(467,'Is each commit deployed to lower env automated? can any commit be deployed to production in an automated way?',172);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(468,'Can components be deployed to container-based?',173);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(469,'Are components built to support elastic scalability?',173);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(470,'Do components support a clear separation between computing and storage during deployment?',173);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(471,'Are there any other restrictions in the components that make them less efficient or effective when running on the public cloud?',173);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(472,'Are components abstracted from specific backup, provisioning, network topology, etc?',173);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(473,'Is the team creating Infrastructure as code and choosing optimal cloud configurations?',173);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(474,'Are teams following the test pyramid approach?',174);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(475,'Are teams following test-driven development and tests effective?',174);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(476,'What is test automation strategy?',174);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(477,'Are test environments available? ',174);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(478,'Are teams creating mocks to cover all scenarios? ',174);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(479,'Do you create specific and complete data sets?',174);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(480,'Do you follow accessibility guidelines while implementing stories?',175);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(481,'Is Team aware of its user base diversity and accessibility support required?',175);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(482,'Do you follow some standard logging patterns?',176);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(483,'Do you have log levels defined for environments?',176);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter) VALUES(484,'How request tracing works?',176);

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (164,'ONE','Requirements have not been defined in detail. All possible ways to arrive at a solution have not been thought about. NFS is not captured');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (164,'FOUR','Requirements have been detailed, not consistent and of high quality. NFS  are incomplete. Business value is not assessed completely with the potential solution');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (164,'FIVE','Requirements have been detailed, consistent and of high quality. NFS has been detailed and frequently updated. Alternative solutions to the potential solution have been compared and evaluated. Business value is always assessed with the potential solution');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (165,'ONE','No detailed design and approach to describe the future state and user journey and process flows are not available');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (165,'THREE','Detailed design and approach to describe the future state is and user journey and process flows are redundant and not updated frequently');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (165,'FIVE','Detailed design and approach to describe the future state and user journey and process flows are updated regularly and used for all discussions');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (166,'ONE','No architecture runway is available. CFRs are not defined.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (166,'THREE','Architecture diagrams and models (ie. C4) are created but it is difficult to find. architecture tech debt is identified and prioritized regularly. There are not enough documentation or sessions available and teams are working silos.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (166,'FIVE','Architecture diagrams and models (ie. C4) are created and available to everyone to be consumed. architecture tech debt is identified and prioritized regularly. There is enough documentation or sessions available to make everyone understand the big picture.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (167,'ONE','Decisions are taken in a silo, and no record is captured. No documentation is available.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (167,'TWO','Some of the important decisions are captured but they get outdated with time,  Functional documents are manually created. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (167,'THREE','All important decisions are captured and maintained in form of ADRs,  Functional documents are manually created so they get outdated easily. docs are not standardized and scattered, so it''s difficult to find stuff.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (167,'FOUR','All important decisions are captured and maintained in form of ADRs, System generates capabilities and functional documents in form of live specifications through well-described tests. docs are not standardized and scattered, so it''s difficult to find stuff.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (167,'FIVE','All important decisions are captured and maintained in form of ADRs, System generates capabilities and functional documents in form of live specifications through well-described tests. docs are standardized and well placed centrally to be consumed by teams.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (168,'ONE','No local checks on code for linting or secret scanning, Tests are missing or poorly written. The team is missing XP practices and tech debt identification understanding. Development gets delayed because of external dependencies.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (168,'TWO','The team follows some of the XP practices but still misses effective tests and fail-early setup, most of the linting or scanning happens over pipeline only. A team missing tech debt awareness. External dependencies slow down development.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (168,'THREE','Team follows XP practices and have fail-early setup where most of unit tests, linting or scanning happen in local machine via pre-commit hook, Tests are not effective and just provide code coverage sense, Tech debts are identified but not picked up. Team doesn''t do refactoring . External dependencies are sometimes mocked to speed up development.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (168,'FOUR','The team follows XP practices and has a fail-early setup where most of the unit tests, linting or scanning happen in a local machine via a pre-commit hook, Tests are written effectively with the right data set, and Tech debts are identified but not picked up. The team doesn''t do refactoring. External dependencies are sometimes mocked to speed up development.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (168,'FIVE','The team follows XP practices and has a fail-early setup where most unit tests, linting or scanning happen in a local machine via a pre-commit hook, Tests are written effectively with the right data set, and Tech debts are identified and picked up in each iteration. The team does refactoring as and when they work on any task. External dependencies are mocked to speed up development.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (169,'ONE','The team has a different-2 local setup and there is no standardised way to onboard a new dev, It takes a large re-effort every time. The team missed domain knowledge and security awareness overall.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (169,'THREE','Dev onboarding is standardized, There are documents available for set-up to follow, It takes a few hours for complete setup but there can be different-2 setups within a team. Also, we have pre-recorded sessions/documents on the domain of work, larger picture and security and keep the team informed.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (169,'FIVE','Dev onboarding is standardized and checklist-driven, Everyone in the team follows the same setup and there are automated scripts available wherever possible, it takes just a few mi to a few hours for a complete setup. Also, we have pre-recorded sessions/documents on the domain of work,  larger picture and security and keep the team informed.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (170,'ONE','Developers don''t follow any standards for commits and ad-hoc branches for features and difficult to manage');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (170,'TWO','Team creating multiple branches. Lots of merging effort. The commits are not atomic and difficult to port to other branches');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (170,'THREE','The team has definite branches for development/staging/releases. The commits are not atomic. The commits are not traceable from the story card and merging commits between branches is difficult.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (170,'FOUR','The team follows trunk-based development and uses feature toggles. The commits are atomic and traceable from the story card. Easy to merge / cherry-pick to other branches');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (170,'FIVE','The team follows trunk-based development and uses feature toggles. All commits are atomic and follow org standards in messages.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (171,'ONE','Adhoc Database changes happen directly through the GUI interface, No DB change versioning exists, Backups are not taken and no monitoring is in place. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (171,'TWO','Database migration changes are neither captured as code nor version controlled. Backups are not taken and tested periodically. Basic monitoring is there but we look and react manually.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (171,'THREE','Database migration changes are captured as code and version control. Backups are not taken and tested periodically. Monitoring is there but we look and react manually. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (171,'FOUR','Database migration changes are captured as code and version control. Backups are taken and tested periodically. Monitoring is there but we look and react manually.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (171,'FIVE','Database migration changes are captured as code and version control. Backups are taken and tested periodically. Monitoring and alerts are enabled as applicable.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (172,'ONE','No build pipeline or build tool. No deployment pipeline. The team manually creates deployment artefacts and deployed manually.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (172,'TWO','No automated build and deployment. Some tests exist. They are neither automated and repeatable on every build nor comprehensive coverage. Some security scanning tools exist');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (172,'THREE','"No automated deployment. An automated build (CI) pipeline is in place
 BUT WITHOUT
 - various levels of test suites,
 - monitoring and alerts set up to detect build/deploy failures early
 - security scanners"
');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (172,'FOUR','Automated build  (CI) and one-click deployment pipeline is in place with
    - monitoring and alert setup to detect build/deploy failures early
    - security scanners
   - only selected commits are deployed to production
');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (172,'FIVE','An automated build and deployment (CI/CD) pipeline is in place.
    - Every commit goes to production
    - monitoring and alert set up to detect build/deploy failures early
    - security scanners
');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (173,'ONE','Components are not containerized and are tightly coupled in nature. Infrastructure is manually handled. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (173,'TWO','Components are containerized and tightly coupled in nature. components can not scale horizontally. Infrastructure is created manually once with some configs and policies (scaling, logs etc) which are never corrected back. Resources are over-provisioned or under-provisioned.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (173,'THREE','Components are containerized and loosely coupled in nature. components might not scale horizontally easily. Infrastructure is created manually once with some configs and policies (scaling, logs etc) which are never corrected back.
');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (173,'FOUR','Components are containerized and loosely coupled in nature. components can scale horizontally easily. Infrastructure is created manually once with some configs and policies (scaling, logs etc) which is partly optimized. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (173,'FIVE','Components are containerized and loosely coupled in nature. components can scale horizontally easily. Infrastructure as code exists with optimal configs and policies (scaling, logs etc). ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (174,'ONE','Poorly written tests here and there without any strategy in place, There is no automation exist. External dependencies make the system non-testable.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (174,'TWO','Following the test pyramid approach partially with some test layers, Tests are not written with the right dataset. The test environment is not available and automation does not exist. Mocks are not used in the lower environment to cover all scenarios depending on external services. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (174,'THREE','Following test pyramid approach partially with some test layers, Tests are written with right datset on each layer. Test environment is not available and automation exist partially . Mocks are not used on lower environment to cover all scenario depending on external services. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (174,'FOUR','Following the test pyramid approach optimally with all layers, effective tests are written with the right dataset on each layer. The test environment is available and automation exists partially. Mocks are not used in lower environments to cover all scenarios depending on external services. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (174,'FIVE','Following the test pyramid approach optimally with all layers, effective tests are written with the right dataset on each layer. Test environment available and automation exist. Mocks are used in the lower environment to cover all scenarios depending on external services. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (175,'ONE','No accessibility guidelines are available and the team does implementation without considering the accessibility aspect. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (175,'THREE','Standard accessibility guide avaialble and followed by team in implementation some times . Some of  developed solutions support defined accessibility requirements to some extent.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (175,'FIVE','Standard accessibility guide available and followed by the team in implementation. All developed solutions support defined accessibility requirements.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (176,'ONE','Log levels and patterns are used randomly, and request or actions are not traceable.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (176,'THREE','Log levels and pattern are standardize, Each request or action can be traced to some extent only.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference) VALUES (176,'FIVE','Log levels and patterns are standardised, logs are combined and each request or action can be traced across systems using co-relation ids.');

-------------------------------------------------------------------------------------------------------
SELECT setval('category_seq',(select max(category_id)+1 from tbm_assessment_category),false);
SELECT setval('module_seq',(select max(module_id)+1 from tbm_assessment_module),false);
SELECT setval('topic_seq',(select max(topic_id)+1 from tbm_assessment_topic),false);
SELECT setval('parameter_seq',(select max(parameter_id)+1 from tbm_assessment_parameter),false);
SELECT setval('question_seq',(select max(question_id)+1 from tbm_assessment_question),false);