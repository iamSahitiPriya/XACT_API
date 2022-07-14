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

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 64,'How would you define the product operation function in your organization?  ', 70);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 65,'How are processes defined and managed within the product team concerning areas of product operations (delivery/release process/change management/communication/ etc.)handled?', 71);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 66,'How are the product management tools identified, evaluated, and rolled out for the team''s usage?', 72);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 67,'what role does product operation function play in internal team on-boarding ?', 73);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 68,'How would you define the customer on-boarding processes in your organisation?', 74);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 69,'what role does product operations play in internal training for your organization?', 75);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 70,'what role does Product Ops play in product analytics ?', 76);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 71,'How would you explain the role of Product Ops in managing the defects data that flows in from product support?', 77);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 72,'How effectively product ops enhances creation of Customer Training ( Collaterals , B2C ) ?', 78);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 73,'Explain the process around competitive data analysis and insights?', 79);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 74,'Briefly explain the processes you have around product feedback analysis and response.', 80);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 75,'How would you define the operations role in the  production support process?', 81);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 76,'What is the role of Product Ops in handling product disruption (break inflow )in production?', 82);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 77,'What is the role of product ops in selection of Tools, Templates selection & Experimentation execution ? ', 83);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 78,'Experimentation process ( Data set , archival ) & Loser Hypothesis artifact preservation', 84);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 79,'How does your organisation /product (operations) team roll out industry best practices?', 85);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 80,'Explain briefly about your processes for certification and regulatory compliance in the industry/market you operate?', 86);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 81,'How do you ensure process efficiencies in your organisation/product teams?', 87);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 82,'What holds true for your Vendor Evaluation & Selection process?', 88);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 83,'How does your organisation manage the vendor partnerships on a continuous basis?', 89);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 84,'Establishing Communication Channels & Process ?', 90);

INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 85,'How would you state your product evolution process and  measures  used for continuous evaluation ?', 13);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 86,'How is business model arrived at?', 14);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 87,'How strategic bets are identified, defined and shared across the organisation?', 15);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 88,'How do you identify and consistently evaluate your competition &  your key differentiators?', 16);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 89,'What all stands true for measures of success in organization?', 17);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 90,'Where do you stand with respect  to your go to market strategy?', 18);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 91,'What all stands true for product analytics for the organisation?', 19);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 92,'How would you define the product management archetypes in your organisation?', 20);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 93,'what would stand true for your organisation when it comes to the level of influence the product  management role has in the organisation', 21);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 94,'How frequently does product team engage in  why  where &  when  exercise before working on an initiative?', 22);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 95,'Where is the larger focus of Product management group?', 23);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 96,'Do product management identify themself as stakeholders?', 24);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 97,'Do product management have visibility on key aspects (roadmap/timelines)of working of its multiple delivery stakeholders?', 25);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 98,'Where do you stand on product capabilities on how to launch?', 26);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 99,'What factors influence the when and why of launch ?', 27);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 100,'What are true for internal & external feedback cycle for your product/feature?', 28);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 101,'What stands true for your organisation on being a Thought leader/Influencer in the market/Industry you operate.', 29);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 102,'What are true for identifying new features/product ideas to be developed?', 30);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 103,'How are the new trends identified & Impact assessment done for the industry and specifically for your business?', 31);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 104,'Whats your backlog governance model (Who owns the backlog; prioritization,review and how frequently its updated)', 32);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 105,'Backlog Refinement/Grooming', 33);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 106,'How are the Epics conceived and managed & how well are the Epics and their functional/business impact understood by the team developing them', 34);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 107,'Business Value Epic brings and how its arrived.', 35);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 108,'What details are covered in the user stories ? Is there any standard and consistent format used and are principles of user story writing that are followed ?', 36);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 109,'How are the stories sliced (where needed)?', 37);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 110,'What is the Feedback Triaging & flow process followed in the organisation?', 38);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 111,'What holds true for customization process multi-tenant ?', 39);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 112,'What holds true for customization process on premise ?', 40);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 113,'How is Feature prioritization done in your organisation?', 41);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 114,'How often is product alignment done with respect to Regulatory , Compliance & Legal  changes ?', 42);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 115,'Does team has hypothesis that validates feature sunset ?', 43);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 116,'How do you manage and handle version sunsets?', 44);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 117,'Frequency with which key objectives , regulation adherence & trigger points are decided & updated for an initiative/ Product sunset', 45);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 118,'How is user(Customer) research done  and what is the frequency of user research ?', 46);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 119,'How effectively is market and user segmentation captured in user experience modelling ?', 47);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 120,'What stands true for experience practices in your organization ?', 48);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 121,'How and when are stakeholders identified and how often product is socialised with all stakeholders?', 49);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 122,'How is intra-team communication set up among various product teams and how are dependencies  mapped, tracked and prioritized ?', 50);
INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
VALUES ( 123,'what all stands true for managing departmental dependency management ?', 51);
-- INSERT INTO tbm_assessment_question(question_id,question_text, parameter)
-- VALUES ( 124,'', 13);

-----------------------------------------------------------------------------------

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 70, 'ONE', 'No proper support /operations functions identified and almost all activities are done by the team managing the product (pre-sales,marketing, production support, collateral creation). No clear processes exist and is executed on adhoc or need basis.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 70, 'TWO', 'The support /operations functions exist in minimum and the product manager fills the gaps in the interim when there is a shortfall /need/requests from other functions for various operational requirements. Note: this can be a sensible default as well in some cases(smaller set up)');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 70, 'THREE', 'Organization understands & realizes that there should be a function which should be responsible for product operations which will be operational throughout and has a person or two dedicated for this role (no full-fledged setup), who will be responsible for the  various functions expected of product operations ( e.g., collaboration with Customer success teams , analytics , tools templates & process adoptions)');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 70, 'FOUR', 'Organization has a separate Product Ops function composed of senior product and operation professionals. This function will be be completely responsible for all the ctivities expected of product operations e.g., collaboration with Customer success teams , analytics , tools templates & process adoptions). Also looks at automation where possible');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 70, 'FIVE', 'Organization has a separate Product Ops function composed of senior product and operation professionals . Apart from defining and executing the prod ops activities, the focus is  on cost optimization and automation of tasks and processes to simplify operations. (usually in enterprises having complex and multiple product lines with high operational complexity).');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 71, 'ONE', 'There are no set processes defined. The teams establish ways of working that are comfortable to them and follow the same. Any changes to the ways of working are identified during the course and implemented.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 71, 'TWO', 'The organization follows an industry-standard release process that is defined at the organization level and monitored by a governance team at the org level. The rest of the subprocesses are addressed as part of that protocol.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 71, 'THREE', 'The product management team along with delivery identifies the various processes that need to be implemented for the smooth functioning and delivery of product releases. Also, processes within the team for new member onboarding/training, team meetings, knowledge management, etc are identified and defined by the team for efficient functioning.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 71, 'FOUR', 'The product operations function works closely with the product and delivery teams and identifies the various processes that need to be implemented for the smooth functioning and delivery of product releases. Also, processes within the team for new member onboarding/training, team meetings, knowledge management, etc are identified and defined by the team for efficient functioning.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 71, 'FIVE', 'The product operations function works closely with the product and other delivery teams and identifies the various processes that need to be implemented for the smooth functioning and delivery of product releases including release process, tools to be used, training, onboarding, and other process needs. They also work towards automating most of these processes to bring in operational efficiencies and cost optimizations.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 72, 'ONE', 'There is minimum or little usage of product management tools and where required is identified and implemented on a need basis.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 72, 'TWO', 'The product management team identifies the tools to be used for various functions such as requirements, roadmap, strategy, planning, content and knowledge management, etc and selects the industry standard tools for their working. The team is then given training on the tools and templates are created where required (e.g user story template, roadmap template)and maintained in the tool for standardization and re-use by the teams. Where multiple product lines exist, different tools are used by the teams for their respective product lines.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 72, 'THREE', 'The product operations function is responsible for the identification, evaluation, and rolling out of various tools to be used for various functions within the product management team. Where multiple product teams exist, the requests for tools are collected and maintained in a central location.  The tools are evaluated by Product Ops  based on various parameters such as the product, market /industry they operate, budget, efficiency etc and once selected,  plan out a detailed training for the same and templates are created (e.g user story template, roadmap template)and maintained in the tool for standardization and re-use by the teams.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 73, 'ONE', 'The organization assumes that each internal team will have collaterals, KT documents as well as basic access to tools documented which ensure smooth on-boarding to internal team members. There is no Product Ops function.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 73, 'TWO', 'The product management team ensures an internal team on-boarding process exists which mandates each team to have Product collaterals, KT Documents, and access to the IT Support team for raising its tool access request for a smooth on-boarding.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 73, 'THREE', 'Product Ops function ensures internal team onboarding process which mandates creation & update frequency for each team to have following documents in place for on-boarding 1) Product Collaterals, 2) Tool Skill-set Required 3)Checklist for tool access provisioning. 4) Project timeline and major impact decisions. (Eg For the Marketing team -- Why the decision to launch in a specific demographic with a specific product launch offer )');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 73, 'FOUR', 'Product Ops function ensures internal team onboarding process is reviewed periodically & incorporates the feedback of new team members. Internal team on-boarding process mandates creation & update frequency for each team to have the following documents in place for onboarding 1) Product Collaterals, 2) Tool Skillset Required 3)Checklist for tool access provisioning. 4) Project timeline and major impact decisions.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 73, 'FIVE', 'Product Ops function ( Multiple product lines) ensure internal team onboarding process is reviewed periodically & incorporates feedback on following parameters,1) Product evolution, 2) market evolution, 3)Change in organization ways of working 4 ) Feedback from new team members. Internal team onboarding process mandates creation & update frequency for each team to have the following documents in place for onboarding 1) Project Collaterals, 2) Tool Skillset Required 3)Checklist for tool access provisioning. 4) Project timeline and major impact decisions.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 74, 'ONE', 'The organization assumes that its customer-facing team will have Product Collaterals, KT documents, Product demos which will assist in the smooth onboarding of customers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 74, 'TWO', 'The product management team ensures a customer onboarding process exists which mandates the responsibility of multiple teams in the customer onboarding process.Each team have Product collaterals, KT Documents, and access to IT support team & product support team for issue triaging of customers');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 74, 'THREE', 'Product Ops function ensures customer onboarding process exists and reviewed frequently. The customer onboarding process has multiple stages & mandates the responsibility of teams in each stage. Each team has Product collaterals, KT Documents, and supportive FAQ documents. They also have access to the IT support team & product support team for issue triaging of customers. Common issues, SLA, and escalation matrix is defined for each stage.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 74, 'FOUR', 'Product Ops function ensures customer onboarding process exists which is reviewed frequently concerning 1) product evolution 2) Change in organization ways of working 3 )Customer feedback where applicable. The customer onboarding process has multiple stages & mandates the responsibility of teams in each stage. Each team has Product collaterals, KT Documents, and supportive assistance documents. They also have access to the IT Support team & product support team for issue triaging of customers. Common issues, SLA, and Escalation matrix is defined for each stage. Product analytics and trigger points defined at various levels to gauge product usage patterns and onboarding experience are continuously updated using these metrics.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 74, 'FIVE', 'Product Ops function ensures customer onboarding process exists which is reviewed frequently concerning 1) product evolution, 2) change in organization ways of working 3 )Customer feedback where applicable. The customer onboarding process has multiple stages in customer onboarding using frameworks & mandates the responsibility of teams in each stage. Each team has Product collaterals, KT Documents, and supportive assistance documents. They also have access to the IT Support team & product support team for issue triaging of customers. Common issues, SLA, and escalation matrix is defined for each stage. Product analytics and trigger points defined at various levels to gauge product usage patterns and onboarding experience are continuously updated using these metrics. The customer feedback mechanism is there to monitor , categorize and used actively to enhance customer on-boarding.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 75, 'ONE', 'The organization assumes that each team will identify its own collaterals, internal training needs and assessment conducted regularly. It''s expected that team heads will be doing gap analysis in training requirements and ensure that internal training is available for the team to consume.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 75, 'THREE', 'The product management team ensures each internal team and operations function will have collaterals, internal training and assessment conducted regularly. Training sessions are conducted before the rollout of product enhancements. Feedback is sought but seldom analysed and acted upon.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 75, 'FIVE', 'Product Ops function ensures internal team training and assessment are conducted regularly. Batch training & assessment is conducted before every feature rollout. One off training sessions and team building exercises are conducted and feedback sought. The feedback is analysed and used as input for further improvements.Training alignment is done regularly concerning changing market dynamics.');


INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 76, 'ONE', 'Organization assumes that it''s Product teams responsibility to implement product analytics as well continously aling product analytics with changing needs of organization , product. Product Analytics and its monitoring isn''t regular activity but a quarterly activity.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 76, 'TWO', 'Product team is responsible for product analytics implementation , execution via product analytics functions. Key product metrics are thoroughly monitored and key changes in market dyanmics , organization structure  , product changes are mapped by product team with help of product analytics function.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 76, 'THREE', 'Product Ops is responsible for product analytics implementation , execution via product analytics functions. Key product metrics are thoroughly monitored around  changes in 1) market dyanmics 2) organization structure  , product changes are mapped by product ops with help of product analytics function.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 76, 'FOUR', 'Product Ops is responsible for product analytics implementation execution via product analytics functions .Product metrics are thoroughly monitored across following 1) Product releases 2) Market dynamics 3) Experimentation & Hypothesis .Product Op''s is responsible to aling offline feedback with Product analytics and validate product analytics with multiple mediums.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 77, 'ONE', 'The product team helps the support team in tracking and closing product defects raised. The product defects data stays with the support team and there is no analysis done on the defects data  to gather insights for further action/plan.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 77, 'TWO', 'The product defects data is analysed manually by the product management team for a period of time usually during a new feature launch to ascertain the quality and type of issues faced by customers.However this is not done on a continuous basis and also lacks further action plan based on the insights.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 77, 'THREE', 'The product management team actively participates in product support at the time of product launch and also on a continuous basis by having designated people allocated for this activity. They are also responsible for detailed analysis and classification of defects data, gather insights and idenitfy suitable action plan/recommendations based on the same to the product manager/head for implementation.When product management does this activity its a big drain of effort on them.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 77, 'FOUR', 'The product operations function (single/multiple product lines/streams) does on a continuous basis, classification and analysis of identified product defects using appropriate tools and provide meaningful insights and recommend further course of action that the product manager can take based on the analysis. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 77, 'FIVE', 'The product operations function (single/multiple product lines/streams) does on a continuous basis,classification and analysis of identified product defects using appropriate tools and provide meaningful insights and recommend further course of action that the product manager can take based on the analysis. Product Ops function views are represented in stakeholder management meetings.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 78, 'ONE', 'Organization assumes that customer training collaterals ,FAQ documents , demo sessions are sufficient to ensure smooth on-boarding for customers. Customer would reach out to Call Centre team which is well equipped to handle customers complaints. There is no Product Ops function.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 78, 'TWO', 'Product management team ensure customer training collaterals ,FAQ documents , demo sessions are sufficient & continuously updated to ensure smooth on-boarding for customers. Customer would reach out to CC team which is well equipped to handle customers complaints. Customers complaint to CC is separately categorized though no action is done on this.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 78, 'THREE', 'Product Ops function ensure  customer training collaterals ,FAQ documents , demo sessions are sufficient & continuously updated to ensure smooth on-boarding for customers. Customer would reach out to CC team which is well equipped to handle customers complaints. Customers complaint to CC is separately categorized and continuous corrective action is taken which includes adding new collaterals , sending newsletters about new features introduction etc.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 78, 'FOUR', 'Product Ops function ensure  customer training collaterals ,FAQ documents as well as basic demo sessions are sufficiently & continuously updated to ensure smooth on-boarding for customers. Customer would reach out to customer care team which is well equipped to handle customers complaints. Customers complaints are separately categorized and continuous corrective action is taken which includes adding new collaterals , sending newsletters about new features introduction etc. Analytics is applied for collaterals , FAQ documents and demo session and the resultant data is analysed by Product Ops to understand customer on-boarding effectiveness and usage patterns. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 78, 'FIVE', 'Product Ops function ( Multiple product lines)  ensure   customer training collaterals ,FAQ documents as well as basic demo sessions are sufficiently & continuously updated to ensure smooth on-boarding for customers. Customer would reach out to customer care team which is well equipped to handle customers complaints. Customers complaints are separately categorized and continuous corrective action is done to enhance collaterals. In addition , the Product Ops also uses  analytics where in specific metrics are marked for the different collaterals and analysed by Product Ops to understand customer on-boarding and usage patterns for single and inter connected product lines. This opens up avenues for Product Ops function to recommend enhancements to existing product lines or even create new product lines ( e.g customers using a functionality for different purpose than intended)');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 79, 'ONE', 'Organization assumes that it is product managers responsibility to gather and assess competitive data insights .Product manager would have continuous discussion with on field teams and keep a continuous tab on all competitors & their growth. This results in operational overhead on product managers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 79, 'THREE', 'Product management team ensure continuous assessment of competitors through various on field teams & marketing teams and also use secondary market research  to analyse and understand competition on various parameters. Where needed they also engage external vendors for competitor product / market analysis and also accommodate any changes needed to the roadmap/planning to stay ahead of competition.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 79, 'FIVE', 'Product Ops function does  continuous assessment of competitors through various on field teams & marketing teams. They have regular discussions with various teams to analyse and understand competitors growth trajectory. where needed they would engage external vendors for competitor product / market analysis and work hand in hand with product managers to evaluate competitors product enhancements, competitors teams  operations analysis and do table stakes neutralization. Product Ops plays critical role in tracking competitions and ensure that the organization adapts and responds to those changes quickly and effectively.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 80, 'ONE', 'There are no formal feedback channels except product support that provides defects resolution.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 80, 'TWO', 'Though there are established feed back channels for customers, they are not actively tracked and analysed by the organisation due to lack of sufficient staff. Only defects raised by customers are addressed based on criticality with the help of a minimally staffed product support team.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 80, 'THREE', 'The organisation has established proper feed back channels both online and offline like CSAT, NPS,customer care/support/social media and certain identified product metrics to track product adoption and customer retention. The product management is responsible for extracting , analysing and classifying the feedback received and also prioritize them for backlog and subsequent delivery.This is usually done at the time of product planning during scope definition.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 80, 'FOUR', 'The organisation has established proper feed back channels both online and offline like CSAT, NPS,customer care/support/social media and certain identified product metrics to track product adoption and customer retention. The Product Ops  is responsible for extracting ,analysing and classifying the feedback received and is done at regular intervals so that customer needs are looked into and addressed. Based on the analysis, the Product Ops come up with suitable recommendations and work with stakeholders for backlog prioritization and subsequent delivery. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 80, 'FIVE', 'The Product Ops  is responsible for extracting ,analysing and classifying the feedback received and is done at regular intervals so that customer needs are given priority. Based on the analysis, the Product Ops come up with suitable recommendations and work with stakeholders for backlog prioritization and subsequent delivery. Where multiple product teams/ lines are involved, the feedback so analysed is presented to all the product heads for their understanding and further plan of action. The feedback analyzation by products ops is also done with respect to product positioning and relevance of the product which cant be done by individual product teams.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 81, 'ONE', 'The product support team is a team formed during the product release  and includes people from various teams to help handle production defects related to product. The team is responsible for aliasing with delivery stakeholders to get the defect fixed,tested and move it production. Usually part of this team dissolves after a certain period once the product release stabilises in production.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 81, 'TWO', 'The product support team is a small team with a simple process defined to handle all kind of defects. The support team tracks all the incoming defects and loops in product management where required to identify and close product related defects.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 81, 'THREE', 'The product support team that has various levels of support identified depending on the criticality of the defects. The support team has an identified stakeholder participating from Product Ops/management to help identify,analyse, track and close product defects flowing in. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 81, 'FIVE', 'The Product operations function is responsible for defining and establishing the process for assisting the product support in handling of defects in line with the SLA and  support levels defined as part of the product support team.  The Ops function identifies and designates individuals  who will be responsible for identification/classification/tracking/closing various products defects by coordinating with stakeholders. The designated individual also creates insights on the defect data and make appropriate recommendations to improve on areas of concern.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 82, 'ONE', 'The organization assumes that each support team will have their part in the workflow that they oversee along with prerequisite conditions for that workflow clearly defined. The expectation is that teams establish ways of working that are comfortable to them and follow the same. Support Teams are mature enough to identify product disruptions & coordinate among themselves if they see an exception to the regular flow of the product in production. The assumption in practice is far from reality.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 82, 'TWO', 'Product management defines support team R & R and product workflow that they oversee along with prerequisite conditions. Expected exceptions paths and ways of working are defined. The product team gets involved as soon as product flow breaks and is intimated by the support team. They assist the support team as well as works with the delivery team and other operations teams to triage the issue and ensure flow is maintained in some other way.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 82, 'THREE', 'Product Ops function defines internal team R & R and product workflow that they oversee along with prerequisites conditions. Expected exceptions, trigger points, ways of working, and escalation matrix are clearly defined. Product Ops ensures various supportive analytics trigger points are created which enables Product Ops to identify,triage flow disruption, and do a quick impact assessment. Product Ops can gauge the required path for bringing quick closure to the disruption.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 82, 'FIVE', 'Product Ops function defines internal team R & R and product workflow that they oversee along with prerequisites conditions. Expected exceptions, trigger points, ways of working, and escalation matrix are clearly defined. Product Ops ensures various supportive analytics trigger points are created which enables Product Ops to identify,triage flow disruption, and do a quick impact assessment. Product Ops can gauge the required path for bringing quick closure to the disruption. Post this, disruption categorization along with RCA. is done. The creation of Long term corrective action plan is within the scope of Product Ops.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 83, 'ONE', 'Organization assumes tools selection , hypothesis creation , experimentation and experimentation duration is a function of the product management team. Product teams will interact with support teams and the support team is mature enough to identify an anomaly and solve it accordingly.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 83, 'TWO', 'The product management team defines templates for the hypothesis  (Hypothesis creation, reason, pre-requisite, validation & approval ) & does tool selction. The product team is responsible for ensuring experimentation execution, analysis,& selecting the winning experiment. The product team will analyze, recommend & give go-ahead for implementing the hypothesis based on experimentation results.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 83, 'THREE', 'Product Ops function does tools selection & defines templates for the hypothesis. (Hypothesis creation, reason, pre-requisite, validation & approval ). Product Ops is responsible for ensuring compliance on Experimentation process & activities which mandate experimentation execution, analysis, winning experiment recommendation based on experimentation results. Product Ops defines the role of support teams in the experimentation process. Product Ops ensure looser hypothesis removal & application user experience is consistently evaluated.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 83, 'FIVE', 'Product Ops function does tools selection & defines templates for the hypothesis. (Hypothesis creation, reason, pre-requisite, validation & approval ). Product Ops is responsible for  ensuring compliance on Experimentation process & activities which mandate experimentation execution, analysis, winning experiment recommendation & looser hypothesis removal based on experimentation results. Product Ops defines the role of support teams in the experimentation process. Product Ops sets up offline feedback communication channels ( Customer care / Account representatives / Account Mangers) and Product Ops ensure looser hypothesis removal & application user experience is consistently evaluated. Product Ops will prevent winning experiment going live completely until offline feedback is analysed for an identified duration.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 84, 'ONE', 'Organization assumes experimentation frequency & due process to be defined & followed in experimentation is a function of the product management team. Organization does not have any archival process .This extensively increases the workload on product teams.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 84, 'TWO', 'The product team defines due process for experimentation . The product team is responsible for the Experimentation process & activities which mandate experimentation execution, experimentation time-period determination, analysis, winning experiment recommendation , role of support teams in process & how loser hypothesis artifacts are created & maintained.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 84, 'THREE', 'Product Ops function defines due process for experimentation . Product Ops defines, monitors  & Updates regularly experimentation processes & activities that mandate experimentation execution, experimentation time-period determination, analysis, winning experiment recommendation, the role of support teams in the experimentation process. Product Ops sets up the process for looser hypothesis removal & a process for communication of the same to support teams.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 84, 'FIVE', 'Product Ops function defines due process for experimentation . Product Ops defines, monitors  & Updates regularly experimentation processes & activities that mandate experimentation execution, experimentation time-period determination, analysis, winning experiment recommendation, the role of support teams in the experimentation process. Product Ops sets up the process for looser hypothesis removal , artifact preservation , archival  and process for communication of the same to support teams. Product Ops function  is empowered to call on discussion for a re-experimentation run of a looser hypothesis.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 85, 'ONE', 'There is no established ways of rolling out best practices. The Individual teams identify processes that works for them and follow the same.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 85, 'TWO', 'The organisation leadership identifies the critical areas of work ( Delivery , Implementation , Customer service) and the teams are expected to self identify best industry practice & implement them .e.g Agile.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 85, 'THREE', 'The organisation leadership identifies the best practices for critical areas of work ( product management,quality and delivery)  and hires external coaches to train and implement the same in the organisation.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 85, 'FOUR', 'The product operations function is responsible for identification, evaluation and rolling out of various best practices to be followed for various functions within the organisation. Where required new teams are set up, processes defined and implemented by the operations team for smoother operations. It''s Product Ops to decide if external coaches are needed or not.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 85, 'FIVE', 'The product operations function is responsible for identification, evaluation and rolling out of various best practices to be followed for various functions within the organisation. Where Multiple product lines or product teams are involved, the operations teams works closely with all product heads and identifies the best practices suitable for the various functions based on the industry, environment and culture in which the organisation operates. Quite often they also pioneer in defining new best practices for the industry they operate.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 86, 'ONE', 'There is no established process. As and when any  regulatory compliance or certification is required by the customers and raised to the product team, the same is added to the product backlog and prioritized for delivery. Legal and regulatory compliance applicable for individual employees (e.g data privacy compliance) are met on a need basis.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 86, 'TWO', 'There is no established process. How ever, the product team at the time of product planning identifies any certification/regulatory compliance required and adds them to the product backlog for prioritization and delivery.Legal and regulatory compliance applicable for individual employees (e.g data privacy compliance) are also identified upfront during planning and tracked for completion by the product/program office.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 86, 'THREE', 'The product management team has a designated individual who is responsible for tracking certification and regulatory compliance required to be adhered through secondary market research and analysis and adds them to the product backlog for prioritization and delivery. For ccertifications, the team is responsible to ensure that the necessary sign offs are obtained from the certified authority once the product is compliant.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 86, 'FIVE', 'The Product Ops function among various activities is also responsible for regular identification, tracking and managing the mandatory legal and regulatory certification compliance that the product needs to adhere to in the market/ industry they operate. The team uses secondary market research and also subscribes to updates from regulatory bodies applicable to their working and where required identifies the vendors to work with for such requirements and adds them to the product backlog for prioritization and delivery. Where multiple product lines exist, they are responsbile for notifying the same to all products and help them prioritize for delivery. For ccertifications, they are responsible to ensure that the necessary sign offs are obtained from the certified authority once the product is compliant.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 87, 'ONE', 'There is no measure or check in place as usually its a single team and hence the team continues to follow process and tasks that is comfortable for them. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 87, 'TWO', 'Inefficiencies are identified over a period of time as and when they are encountered and teams come together and resolve the same. This may include fixing gaps in the existing processes/methods or evaluation of some alternate processes/methodology to be followed or adopted.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 87, 'THREE', 'The product management team works towards streamlining processes so that to ensure standardization and minimise inefficiencies.  A designated set of individuals evaluates processes at regular intervals or when there are new methodologies in the industry/market and works towards incorporating the changes within the product teams.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 87, 'FIVE', 'The product operations function works towards streamlining processes so that to ensure standardization and minimise inefficiencies across various product teams. It evaluates processes at regular intervals or when there are new methodologies in the industry/market and works towards incorporating them within the product teams. As the organisation grows and matures, the Ops also look at tools/automation for simple and repetitive tasks to improve efficiency in the ways of working and also focus on cost optimizations.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 88, 'ONE', 'There is no process defined as such. During product development, product team identifies the vendor based on market research and requests for a product demo and decides based on the fitment.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 88, 'TWO', 'There is no process defined as such. Product team identifies the need for a vendor upfront during planning and identifies a couple of vendors who are leaders and requests for a demo and select based on the fitment.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 88, 'THREE', 'The vendor evaluation and selection is done by the product management team.The needs are identified upfront during the planning phase and the vendors are identified by a detailed research and are evaluated using product features, price ,technology and support system. Vendor continuity is assessed.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 88, 'FIVE', 'There is a well defined process for vendor evaluation and selection. At the time of product and Release planning, the vendor dependencies are identified and tracked in a central location. if there are multiple product lines or multiple produc teams that has dependency, the ops team ensures that all product line needs are considered, conducts research and finalises on the top 2 or 3 vendors in that category. A detailed product demo evaluating features, technology, pricing ,support , roadmap and release plans are done for all the selected vendors and then a final decision is made.  The ops team/person responsible defines and coordinates the entire process for the product team and the partnership there of. Also takes responsibility of closure of the integrations for various product lines as identified.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 89, 'ONE', 'The integration is just one time and hence partnerships are not defined. In some cases, the product team integrates with a trial /free version available of the vendor to test the feature and leave it at that.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 89, 'TWO', 'Once the vendor is selected, the product team signs an agreement with the vendor team for integration and scope of work and the vendor provides support for a certain duration as required. The product is certified with the vendor by default for that feature. In case of any defects raised , the vendor provides support as part of their regular support process. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 89, 'THREE', 'As part of the vendor evaluation process itself, factors such as support, continuity are assessed and hence becomes an important criteria for vendor selection.  Vendor Coordination , Vendor continuous evaluation , Vendor training and up-gradation all are governed under a clearly documented vendor SOP . Vendor SOP is created by the product team and  approved by functional heads.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 89, 'FIVE', 'The Product Ops function draws out a detailed process for partnership and support once they identify the vendor using a meticulous selection process.Product Ops have standardized  SOP templates .Vendor Coordination , Vendor continuous evaluation , Vendor training and up-gradation all are governed under a clearly documented vendor SOP . Vendor SOP is created by the product team and  approved by functional heads. Its expected by organization that the team will seeks inputs from function heads on a frequent basis & will make appropriate variations to Vendor SOP. The SOP also includes SLA for support, pricing, training and conitnuous updates/product fixes needed for the product. ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 90, 'ONE', 'The organization assumes that  the relevant teams will have the workflow prerequisite conditions clearly defined. Expectations are that teams are mature enough to identify and set up ways of working , communication channels , escalation matrix with relevant teams that they would be coordinating in running the day to day operations/interactions within the teams.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 90, 'TWO', 'Product management defines communication channels between various teams. They assist the support teams as well as work with the delivery teams and other operations teams to triage the issue and ensure smooth flow of work.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 90, 'THREE', 'Product Ops defines internal team R & R and product workflow that they oversee along with prerequisites conditions. Expected exceptions, Trigger points, Ways of working, and Escalations Matrix are clearly defined. Product Ops ensures various supportive analytics trigger points are marked which enables Product Ops to identify, triage flow disruption, and do a quick impact assessment. Product Ops can gauge the required path for bringing quick closure to the disruption.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 90, 'FOUR', 'Product Ops defines internal ( organization / vendor) team R & R and product workflow that they oversee along with prerequisites conditions. Expected exceptions, Trigger points, Ways of working, and Escalations Matrix are clearly defined. Product Ops ensures various supportive analytics trigger points are marked which enables Product Ops to identify, triage flow disruption, and do a quick impact assessment. Product Ops can gauge the required path for bringing quick closure to the disruption. Disruption categorization along with Root Cause analysis is done. The creation of Long term corrective action plan is within the scope of Product Ops.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 90, 'FIVE', 'Product Ops defines internal ( organization / vendor) team R & R and product workflow that they oversee along with prerequisites conditions. Expected exceptions, Trigger points, Ways of working, and Escalations Matrix are clearly defined. Product Ops ensures various supportive analytics trigger points are marked which enables Product Ops to identify, triage flow disruption, and do a quick impact assessment. Product Ops can gauge the required path for bringing quick closure to the disruption. Disruption categorization along with Root Cause analysis is done. The creation of Long term corrective action plan is within the scope of Product Ops. Adhoc team changes / disruption management is within the scope of Product Ops which may include vendor responsibilities realignment. ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 13, 'ONE', 'Product evolution is a natural process and there is no near term, midterm, and long term vision.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 13, 'TWO', 'The product vision is created but without customer segments & value segmentation (The value product brings to each customer type) , focusing only on problems and key differentiators. New features are developed only on the near-term business value and not with product vision and alignment in line. Neither product vision is evolved every year.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 13, 'THREE', 'The product vision is refined regularly and new features to be introduced are decided based on  product vision alignment. New features are assessed not only on short-term business value but also on long term vision and business value.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 13, 'FIVE', 'The product vision is refined continuously and new features to be introduced are decided based on any product vision alignment model. New features are assessed not only on short-term business value but also on long term vision and business value. Features that add new equity (customer segment) or increase the equity base are given a higher weightage than near-term business value features.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 14, 'ONE', 'Business/Revenue models are arrived at based on the thought process of our leadership team . Its drawn out based on the understanding of the product/revenue ideas of the leadership. Product market fit is arrived, based on inputs from leadership.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 14, 'TWO', 'Business/Revenue models are arrived at keeping in mind the product vision and involves a discussion with few stakeholders based on their understanding of the market/customers. it does not include all stakeholders  and parameters . Product market fit is assesed based on the stakeholders understanding. No market research is done to identify the target market  and value proposition is not clearly articulated.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 14, 'THREE', 'Business/Revenue models are arrived at keeping in mind the product vision and after extensive deliberation based on the identified target market segment, the value proposition of the product being developed, the partner ecosystem, competition etc.  We use our own way to create the models. Down stream communication of the same to all stakeholders is absent. The product market fit is clearly identified using the models defined.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 14, 'FOUR', 'The organisation clearly understands the components of a Business /Revenue model and also understand the importance of deliberation with all stakeholders to arrive at the same. Framework such as business model canvas is used to arrive at the same and is ensured that it is communicated upstream and downstream to all relevant stakeholders. The model is revised and updated based on the market/industry dynamics in which we operate.The product market fit is clearly identified using the above and defining and testing the MVP /hypothesis with potential customers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 14, 'FIVE', 'The organisation clearly understands the components of a Business /Revenue model and also understand the importance of deliberation with all stakeholders to arrive at the same. Framework such as business model canvas is used to arrive at the same and is ensured that it is communicated upstream and downstream to all relevant stakeholders. The model is revised and updated based on the market/industry dynamics in which we operate.With each business model thats currently in motion SWOT analysis is done .The product market fit is clearly identified using the above and defining and testing the MVP /hypothesis with potential customers with clear and identified set of metrics to measure the success.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 15, 'ONE', 'Our product strategy is based on the thought-process of our leader(CEO).We have not identified pivots/ levers as of now concerning strategy.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 15, 'TWO', 'Our product strategy is based on thought process of our leadership team (CEO/CXO).We have not identified pivots/ levers as of now concerning strategy.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 15, 'THREE', 'Our product strategy is based on our product vision and specific pivots/levers.We do have measures to continuously evaluate our strategy and they are subjective and objective measures & have regular cadence for strategy revision.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 15, 'FIVE', 'Our product strategy is evolved from our product vision and have identified pivots/levers.We do use frameworks for continuous evaluation of pivots / levels on which the product stratergy is based on. Strategy deviations are also marked based on seismic shifts (Technology & regulatory)  in the industry we operate.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 16, 'ONE', 'Competition is evaluated on a adhoc basis using SWOT analysis based on input from marketing/sales. It is also done during new opportunity pursuits. key differentiators are  not well articulated and are not publicised extensively. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 16, 'TWO', 'The product team does a detailed evaluation of competition and keep abreast of the latest features launched by them. However no further actionable outcomes are identified based on the evaluation for furthering the product cause.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 16, 'THREE', 'The organisation outsources the market /competition intelligence to an external vendor who feeds to the product team on a regular basis. Actionable outcomes are identfied and included in the broader strategy and roadmap. Key differentiators are identified and used during new business opportunities/marketing collaterals to articulate the product value.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 16, 'FOUR', 'The organisation has a separate market/competitive intelligence team that actively tracks the competition and market and feeds to the product team on a regular basis.  We also work with external research and analyst firms on the product -market positioning vs the competition and regularly update strategy/roadmap /differentiators based on these inputs.The identified opportunities are then attached a business value and prioritized for product delivery and launch.The key differentiators are identified , articulated and communicated across all stakeholders ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 16, 'FIVE', 'The organisation has a separate market/competitive intelligence team that actively tracks the competition and market and feeds to the product team on a regular basis. We also work with external research and analyst firms on the product -market positioning vs the competition and regularly update strategy/roadmap /differentiators based on these inputs.  The identified opportunities are then attached a business value and goes through OKR process of organization.The key differentiators are identified , articulated and communicated across all stakeholders ');


INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 17, 'ONE', 'MoS are not used / MoS are defined at the org level and does not trickle down to products/team level.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 17, 'TWO', 'MoS are defined after the product is developed, just before launch');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 17, 'THREE', 'MoS are defined at inception but are not evolved during development.  They are either reviewed before or after launch');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 17, 'FOUR', 'MoS are defined at inception and continuously evolved during the development & reviewed at regular intervals after launch');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 17, 'FIVE', 'MoS are defined both in terms of short-term and long-term goals  and with both business outcomes and values in mind.');


INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 18, 'ONE', 'Product marketing team drive the GTM strategy once product is ready for launch. The customer segment/messaging/pricing decisions are adhoc and not a well planned exercise and done with minimal inputs from product and other stakeholders at the time of launch with little or no time to incorporate feedback. The marketing collaterals are not crisp and does not convey the value proposition effectively.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 18, 'TWO', 'The GTM strategy is done as an exercise by the product team once the product is ready for launch. The segment/pricing/Messaging are identified post product devlopment is complete,and lacks clear articulation of the same. Marketing as stakeholders only work on the launch/messaging/ launch materials based on inputs from product team.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 18, 'THREE', 'The GTM strategy is a well planned exercise by the product management team during the product inception/planning phase to identify the right customer segment,product offering,pricing,messaging,product positioning and value proposition for the product. The same is communicated to all stakeholders. Once the product is ready for launch, the marketing collaterals needed for the product launch are created by marketing team with inputs from the product team and other relevant stakeholders.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 18, 'FOUR', 'The GTM strategy is a well planned joint exercise between the product management and product marketing leaders during the product inception/planning phase to identify the right customer segment,product offering,pricing,messaging,product positioning and value proposition for the product with clearly laid out  plan and timelines to execute the same. The marketing collaterals needed for the product launch are created by marketing team with inputs from the product team and other relevant stakeholders at each milestone phase of product development and reviewed. As the product development progresses, the GTM strategy is revisited  and revised if needed.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 18, 'FIVE', 'The GTM strategy is a well planned joint exercise between the product management and product marketing leaders during the product planning phase to identify the right customer segment,product offering,pricing,messaging,product positioning and value proposition for the product with clearly laid out  plan and timelines to execute the same. The marketing collaterals needed for the product launch are created by marketing team with inputs from the product team at each milestone phase of product development and reviewed. As the product development progresses, the GTM strategy is revisited  and revised continously.Before launch, beta rollout is done and final product segmentation and positioning is done depending on the beta rollout feedback.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 19, 'ONE', 'No tracking and visualization.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 19, 'TWO', 'Tracking all possible metrics, without any tie-up to operation goals or measures of success.Visualizing many metrics without clear purpose and insight. All metrics have equal weightage and are not tied towards specific operation or business goals');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 19, 'THREE', 'Tracking and visualizing only basic metrics, infrequent intervals, using canned tools');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 19, 'FOUR', 'Tracking is embedded with a tracking strategy based on specific operation/business goals or measures of success. Product metrics are real-time and easily visible to all relevant business and IT stakeholders. There is a regular cadence for the team to review metrics, develop action items and review progress.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 19, 'FIVE', 'Tracking is embedded with a tracking strategy based on specific operation/business goals or measures of success. Product metrics are real-time and easily visible to all relevant business and IT stakeholders. There is a pre-determined regular cadence for the team to review metrics, develop action items and review progress . Experimentations/hypothesis and weightage-based key metrics are pre-determined and continuously evaluated. Metrics are combined with offline data and measured.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 20, 'ONE', 'The expectations around the role is mostly fluid and domain/industry expertise is what ideally define this role in the organisation. Its mostly a domain SME kind of archetype.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 20, 'THREE', 'Understands product management best practices for delivery and work with delivery teams to deliver products on time. Its mostly a product practitioner kind of archetype');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 20, 'FIVE', 'The organisation has clearly defined archetypes (strategist, practitioner,SME)for product management at various levels which are  evaluated against.  The role comes with various skills/attributes that are must haves/nice to haves to ensure that a balanced team is avaiable that can deliver products of value to its customers. The key attributes/skills include but not limited to...
1.product thinking (the why of product)
2.Design thinking
3.Customer focus and market orientation
4.Communication and Collaboration with all the stakeholders
5.Strategy and innovation
6. Stakeholder influence and management . (Ability to say No where needed)
');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 21, 'ONE', 'The product management is more execution in nature and takes direction from leadership and executes what has been told. Highly solution oriented and has minimal influence in the strategy /goals/vision. It is predominantly sales driven product management.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 21, 'THREE', 'The product management is execution in nature and takes direction from the technical leadership and  driven by delivery capabilities.  It is predominantly engineering driven product management.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 21, 'FIVE', 'The product management is the Pivot and the key team around which all other functions collaborate and deliver products of value to customers. The team  is responsible for the product strategy aligned to the product vision set by the product leadership and execution of the same to deliver value to customers.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 22, 'ONE', 'Product team does not challenge the stakeholders on why , where and when before working on an  initiative ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 22, 'TWO', 'Product team does not challenge the stakeholders on all 3 parameters.However before work on initiative starts,  requests for the why to be  shared by stakeholders.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 22, 'THREE', 'Product team challenge the stakeholders on why , where and when and the initiative is adopted only when at high level ROI is deemed positive.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 22, 'FIVE', 'Product team expects  why , where and when details  to be shared by stakeholders along with Basic ROI estimations. Source validation is discussed before product teams initiates work on ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 23, 'ONE', 'The product management team largely focus on delivery of the product features identified as part of the roadmap. The roadmap is created and owned by the head of the product. Major chunk of the time in a product manager''s day goes into solutioning.  There are no clear demarcation of product owner Vs product manager role and mostly the roles are merged');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 23, 'TWO', 'The product management team works together with the leadership and arrive at the product roadmap based on market demands and customers and work towards delivering those features committed. The roadmap resembles a high level feature list of deliverables rather than the various themes/concepts that the team is working on.  The team has no clarity on big picture, product vision or product goals and kind of of exist in silos with one or two senior level product managers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 23, 'THREE', 'The product team does market research and works closely with executive teams and identifies market needs/customer problems. It is responsible for creating business cases/product strategy and roadmap based on the inputs and actively communicates with all stakeholders. Upon signoff works with product owners/design/engineering to arrive at a usable solution/feature that can be delivered to solve the identified business problems.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 23, 'FIVE', 'The product team does market research and works closely with executive teams and identifies market needs/customer problems. It is responsible for creating business cases/product strategy  ( includes design & ROI estimates) and roadmap based on the inputs and actively communicates with all stakeholders .Upon signoff works with product owners/design/engineering to arrive at a usable solution/feature that can be delivered to solve the identified business problems.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 24, 'ONE', 'Product management team identifies themselves as just mere negotiators of prioritization between multiple stakeholders.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 24, 'TWO', 'Product management team identifies themselves not only as  negotiators among various stakeholders,but also as a team that respresents the point of view technical team as well.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 24, 'FOUR', 'Product management team identifies themselves as stakeholders who have a proper say in prioritization of features and they understand that they command the right to say no.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 24, 'FIVE', 'Product management team identifies themselves as most important stakeholders and  enablers in making any decision a success.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 25, 'ONE', 'The product management team operates in silos and interacts with various stakeholders on a adhoc basis. The roadmap and timelines of various stakeholdes are not always in alignment and the team has no visibility or understanding of the same. The dependencies are not identified clearly and called out.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 25, 'TWO', 'The product management team works along with the engineering team to work out the product release planning based on internal prioritization(inputs from customer facing teams) . The team is flexible and incorporates changes as per inputs received from the engineering team. How ever visibility of new initiatives on either side is not discussed or updated. Other stakeholders like marketing/QA /documentation/sales/implementation teams are informed of the Roadmap/proposed product release with minimal or no option for feedback.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 25, 'FOUR', 'The product management team works closely with heads of all relevant stakeholders (Engineering/Architecture/QA/Sales/Implementation/Documentation etc.) and has visibility into their roadmaps/timelines and actively incorporates into their Roadmaps/product planning/Release planning exercise. This exercise is carried out at said intervals to ensure that all stakeholders are updated and are on the same page. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 25, 'FIVE', 'The product management team works closely with heads of all relevant stakeholders (Engineering/Architecture/QA/Sales/Implementation/Documentation etc.) and has visibility into their roadmaps/timelines and actively incorporates into their Roadmaps/product planning/Release planning exercise. This exercise is carried out at regular intervals to ensure that all stakeholders are updated and are on the same page. The team also ensures regular upstream and downstream communication to ensure alignment with all relevant stakeholders and any feedback , changes , delays are marked and roadmap is continuously updated . ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 26, 'ONE', 'Don''t have the capability to do targeted or precise launch. ( Types of Launch - Canary / Beta )');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 26, 'TWO', 'Have the capability to do soft launch & hard launch but only one can be executed.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 26, 'THREE', 'We do have launch capabilities for precise & targeted launch based on product analytics. ( User Demographic based launch)');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 26, 'FOUR', 'We do have capabilities for soft and hard launch based on geo ip locations. (User demographic based launch soft / hard launch)');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 26, 'FIVE', 'We do have capabilities for launch based on user traffic source .( Organic / Paid , Mass emails , Notifications , Geo location based  organic /paid traffic)');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 27, 'ONE', 'Don''t have any product analytics to support launch. The final launch is done to all defined markets with all marketing collaterals once product is deemed to be ready for launch . ( Ready for launch -- product development complete, supportive scans & infrastructure upgrades completed ) ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 27, 'TWO', 'Have product analytics to support launch . Beta launch is rolled out to a specific customer segment ,with MoS defined for beta and customer segment and based on the outcome, full launch is made.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 27, 'THREE', 'Have product analytics to support launch and the target customer segment to launch is identified. Experimental launch followed by beta launch is done to the identified market only when MMF (Minimum Marketable Feature) criteria is met. ( MMF -- the launch brings does equity maximization )');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 27, 'FIVE', 'Have product analytics to support launch and the target customer segment to launch is identified. Experimental launch followed by beta launch is done to the identified market only when MMF (Minimum Marketable Feature) criteria is met. All launch collaterals are revised based on the experimental launch & beta feedback received from the market segment');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 28, 'ONE', 'No feedback on CX from stakeholders & no appropriate feedback channels from customers.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 28, 'TWO', 'Product showcase occurs after development and feedback is taken into account ,but showcase is done to a very small audience at leadership level. No beta user feedback is sought. The product is directly rolled out and customer feedback is taken into account.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 28, 'THREE', 'Prototype validation is done with pseudo users and prototype showcase is done as well. Product showcase occurs at fixed regular intervals and final showcase at the end of the feature development with all the feedback factored in. Customer feedback channels are established but not frequently monitored and the feedback received are not addressed promptly.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 28, 'FOUR', 'Prototype validation is done with pseudo users and prototype showcase is done as well. Product showcase occurs at fixed regular intervals and final showcase at the end of the feature development with all the feedback factored in. Customer feedback channels are established but not frequently monitored and the feedback received are not addressed promptly. Post rollout, feedback channels are established & frequently monitored and the feedbacks are addressed promptly.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 28, 'FIVE', 'Prototype validation is done with pseudo users and prototype showcase is done as well. Product showcase occurs at fixed regular intervals and final showcase at the end of the feature development with all the feedback factored in. Beta experimentation rollout is done & beta user feedback channels are established, monitored and the feedbacks received are factored in. Post rollout it is completely data-driven.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 29, 'ONE', 'There is minimal/no focus on thought leadership in the organisation. There is minimal representation from the teams on external webinars/panel discussion/blogging or sharing POVs around the industry/market that creates awareness about the product/brand. Mostly a follower than an influencer.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 29, 'TWO', 'The organisation actively participates in industry/market events and also encourages thought leadership among its employees . It also conducts webinars/seminars/conferences relating to the industry/market it operates. It has a good brand recognition, but cannot be termed as an influencer/disruptor.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 29, 'THREE', 'The Organisation encourages innovation and thought leadership among its employees . It establishes guidance and ways of working and is often seen as trying to establish itself a pioneer/thought leader in  the market it operates. Is highly visible and a leading contributor /driver in various forums/conferences');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 29, 'FIVE', 'The Organisation encourages innovation and thought leadership among its employees . It establishes guidance and ways of working and is seen as a pioneer/thought leader in  the market it operates. Regularly crafts new techniques/books/frameworks for the benefit of the peer community and is an influencer and leading contributor /driver in various forums/conferences.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 30, 'ONE', 'Innovation requests are directly coming from Stakeholders ask''s.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 30, 'TWO', 'New innovations are identified by stakeholders purely based on the priority KPI. The request source is marked (from where the request came) and the decision to productize it is considered.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 30, 'THREE', 'New innovations are identified based on multiple KPIs. The request source is kept anonymous (to avoid decisioning bias) and is added to the prioritization bucket.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 30, 'FIVE', 'New innovations are identified based on both additional revenue opportunities as well as value add to end customers while keeping customer segmentation model and  the weightage of current revenue vs future revenue impact. The request source is kept anonymous and is added to the prioritization bucket which is reshuffled every quarter by the PO/PM.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 31, 'ONE', 'We don''t have pivots / levers that can indicate to  us about the change in trends. The CXO team that leads the way for us.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 31, 'TWO', 'We don''t have pivots / levers identified, but we keep a tab on seismic shifts that are happening as part of industry/ domain we operate.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 31, 'THREE', 'We have identified pivots which help us gauze any new trends that may impact our organization and impact assessment for the newly identified trend is done on a need basis.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 31, 'FOUR', 'We have pivots which help us to gauge the general trends as well as trends that are emerging due to disruption in technology / regulatory space . Impact assessment for these are done on a continuous basis .');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 31, 'FIVE', 'We have pivots which help us to gauge the general trends as well as trends that are emerging due to disruption in technology / regulatory space . Impact assement for these are done on a continuous basis .We are the industry leaders and continue to look out for disruptions methodologies that may be deployed from time to time.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 32, 'ONE', 'The Backlog contains list of all the features/stories that needs to be delivered and is created ,maintained and updated by the product owner/manager.There is no framework or tool used and usually prioritized based on gut feel/ stakeholders ask.The updates are not necessarily communicated and done only on a need basis.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 32, 'TWO', 'The Backlog contains list of all the features/stories that needs to be delivered and is created ,maintained and updated by the product owner/manager.There is no framework or tool used for prioritization and is usually done based on gut feel/stakeholder ask. The prioritization is usually done at the start of the product release planning along with development teams and not updated regularly.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 32, 'THREE', 'The Backlog contains list of all the features/stories that needs to be delivered and is created ,maintained and updated by the product owner/manager. The backlog prioritization is a joint exercise that the PO/PM does along with relevant stakeholders using industry standard frameworks. This exercise is done on continuous basis as the release progresses so that the features delivered are in alignment with the market/customer needs. The team ( PO / DEV / QA / Stakeholders ) understand that anyone can add to the product backlog for prioritization.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 32, 'FOUR', 'The Backlog contains list of all the features/stories that needs to be delivered and is created ,maintained and updated by the product owner/manager. The backlog prioritization is a joint exercise that the PO/PM does along with relevant stakeholders using industry standard frameworks and is also in alignment with the product backlog of other downstream /upstream systems that has impact. This exercise is done on continuous basis as the release progresses so that the features delivered are in alignment with the roadmap/market/customer needs. The updates are then communicated to relevant stakeholders for their planning. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 32, 'FIVE', 'The Backlog contains list of all the features/stories that needs to be delivered and is created ,maintained and updated by the product owner/manager. The backlog prioritization is a joint exercise that the PO/PM does along with relevant stakeholders using industry standard frameworks  and in alignment with the product backlog of other downstream /upstream systems that has inmpact. The dependcies are continously tracked and followed up in scrum of scrums. This is done on continuous basis as the release progresses so that the features delivered are in alignment with the roadmap/market/customer needs. The updates are then communicated to relevant stakeholders for their planning. ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 33, 'ONE', 'Backlog grooming/refinement process is not followed as a regular practice. This is usually done at the beginning of a product release.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 33, 'TWO', 'Backlog grooming/refinement process is done on a need basis and the product owner/manager themselves with minimum or no input from other stakeholders (Development team)');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 33, 'THREE', 'Backlog grooming/refinement process is adhoc, with participants from product management , engineering and QA depending on their availability.The team works on all the objectives of the refinement process including breaking down of user stories, pre requisite stories mapping, adding  details to stories for up coming sprints,  decluttering the backlog in line with product roadmap and customer priority and high level estimate for stories where possible.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 33, 'FIVE', 'Backlog grooming/refinement process is a regular process with the cadence  as agreed by the delivery team and involves all the required participants. The team works on all the objectives of the refinement process including breaking down of user stories, pre requisite stories mapping, adding  details to stories for up coming sprints,  decluttering the backlog in line with product roadmap and customer priority and high level estimate for stories where possible.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 34, 'ONE', 'Epic(s) are conceived and written once the product team prioritizes a initiative and usually contains a high level scope of the product/features to be built.  There is no defined format and the team documents it based on their understanding of the initiative.The epics are written with minimal or no inputs from other delivery stakeholders. They lack the big picture of the business goal to be achieved.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 34, 'THREE', 'Epic(s) are created for a initiative that is prioritized in alignment with the product roadmap. The scope of the work and the high level requirements are detailed out in the epic based on discussion/inputs from other delivery stakeholders in an agreed format and reviewed ,signed off and stored in a Central tool. However during grooming/ sprint planning only the user stories are reviewed and detailed out and the alignment to the epic is not considered/diluted over time.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 34, 'FIVE', 'Epic(s) are created for a initiative that is prioritized in alignment with the product roadmap. The scope of the work and the high level requirements are detailed out in the epic based on discussion/inputs from other delivery stakeholders in an agreed format and reviewed ,signed off and stored in a Central tool.Multiple  epics are created based on the complexity of the scope & each epic has weightage of completion to the initative. The user stories are aligned and there is a clear line of sight to the epic to ensure that the delivery teams are aligned to the business value/goal of the epic.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 35, 'ONE', 'The epics contains only the scope of work. Benefits/value are not documented in epics. It remains only at the roadmap level.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 35, 'THREE', 'The epics contains detailed scope of work and articulates high level benefits/advantage of why this epic needs to be delivered. However the business value (quantitative/qualitative) is not articulated.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 35, 'FIVE', 'The epics contains detailed scope of work and articulates high level benefits/advantage of why this epic needs to be delivered. The business value (quantitative/qualitative) is articulated in monetary value terms.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 36, 'ONE', 'The user stories do not follow a defined template and contains details of the feature/requirement to be developed, assumptions and dependencies. The story is discussed and revised multiple times during the sprint as and when new scenarios are discovered.There are no DOR or DOD clearly defined  for the user story to be picked up and completed for a sprint.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 36, 'THREE', 'The user stories follow a defined template and contains details of the feature/requirement to be developed, assumptions and dependencies (Joint Exercise). The story is discussed and revised multiple times before the sprint planning as and when new scenarios are discovered.However there are no DOR or DOD clearly defined  for the user story to be picked up and completed for a sprint.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 36, 'FIVE', 'Defined template and contains details of the feature/requirement to be developed, assumptions,acceptance criteria and dependencies (Joint Exercise). The story is discussed, revised and is aligned to the INVEST principle .The DOR and DOD are clearly defined after discussion among the team for the the user story to be picked up and completed for a sprint');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 37, 'ONE', 'There is no concept of story slicing followed. Stories written are taken up as is for development even if the story points are higher.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 37, 'THREE', 'The stories are sliced based on the complexity and effort involved but does not consider the business value it can deliver.  ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 37, 'FIVE', 'The stories are sliced either vertically or horizontally depending on the feature being developed and also keeping in mind the INVEST principle . It is usually done as an exercise along with other delivery stakeholders.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 38, 'ONE', 'The feedback mechanism to track defects /enhancements raised by customer is very adhoc and  not tracked in a central location. Depending on customer/defect priority they are triaged ,priortized,developed and delivered to customers. The impact/spill over to the sprint currently in execution is not assessed clearly.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 38, 'TWO', 'The feedback mechanism to track defects/enhancements raised by customer is maintained in a central location using a tool . The same is triaged by the product team to be delivered to customer and included in the next sprints/ release as stories .Issues/enhancements are treated alike and are not differentiated.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 38, 'FOUR', 'The feedback mechanism to track defects/enhancements raised by customer is maintained in a central location using a tool.The same is triaged by the product team to be delivered to customer based on the severity .The identified list of defects to be fixed goes into a defect bucket and the identified enhancements are created as stories and  tracked for delivery.The customer is informed of when the same would be delivered.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 38, 'FIVE', 'The feedback mechanism to track defects/enhancements raised by customer is maintained in a central location using a tool . The same is triaged by the product team to be delivered to customer based on the severity .The identified list of defects to be fixed goes into a defect bucket and the identified enhancements are created as stories and goes into backlog for Priortization .The customer is informed of when the same would be delivered.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 39, 'ONE', 'Multi instance based application.Multiple product backlogs are maintained and the incoming customization requests are added to delivery backlog of the team that is supporting the client. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 39, 'TWO', 'Multiple tenant application. There is a single product team and multiple smaller client specific teams . Customization requests are assessed based on its benefit by core product team and is added to core product delivery backlog or else its becomes a part of delivery backlog for the client specific team.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 39, 'THREE', 'Multi tenant application enabled with both SAAS &  VAS functionality .Customization requests are assessed based on its benefit by core product team in terms of long term /short term vision for new feature enhancements or VAS rollout for client . Accordingly the feature is priortized and goes into delivery backlog . Timelines are communicated to client accordingly ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 39, 'FIVE', 'Multi tenant application enabled with both SAAS &  VAS functionality . There is a single product team .Customization request are adjudged based on its benefit by Core product team in terms of long term /short term vision for new feature enhancement or VAS rollout for client . Accordingly the feature is priortized and goes into delivery backlog . timelines are communicated to client accordingly ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 40, 'ONE', 'Multiple on-premise applications .Multiple product backlogs are maintained and the incoming customization requests are added to delivery backlog of the team that is supporting the client. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES (40, 'THREE', 'Multiple on-premise applications. There is a single product team and multiple smaller client specific teams . Customization request are assessed based on its benefit by core product team and added to core product delivery backlog or else its becomes a part of delivery backlog for the client specific team.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 40, 'FIVE', 'Multiple on premise applications. There is a single product team .Customization requests are assessed based on its benefit by core product team in terms of long term /short term vision for new version enhancements & rollout . Accordingly the feature is priortized and goes into delivery backlog . The timelines are communicated to client accordingly');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 41, 'ONE', 'No comparison among feature and henceforth no prioritization');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 41, 'TWO', 'Feature comparison exists and its prioritized by the near term business value  that the initiative will provide.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 41, 'THREE', 'Feature  is prioritized based on both additional revenue opportunities as well as value add to end-users. The Feature request source is kept anonymous and is marked in the prioritization bucket. Feature value is calculated.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 41, 'FOUR', 'Feature are prioritized based on both revenue opportunities as well as value add to end users while keeping customer segmentation model and the weightage of current revenue vs future revenue impact. Feature request source is kept anonymous and it is marked in the prioritization bucket which is reshuffled every quarter by the PO/PM. Prioritization rarely achieves consensus.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 41, 'FIVE', 'Feature are prioritized based on ROI. Feature request source is kept anonymous and it is marked in the prioritization bucket which is continuously reshuffled by the PO/PM team. Prioritization achieves consensus among all stakeholders.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 42, 'ONE', 'Product features initially are conceptualized considering all regulatory , compliance and legal constraints . Product feature sets are tracked with respect to regulatory compliance and legal aspects every year.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 42, 'THREE', 'Product features initially are conceptualized considering all regulatory , compliance and legal constraints . There is continuous check on regulatory and complaince framework that the product adhers to . Major Production release are vetted against the same.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 42, 'FIVE', 'Product features initially are conceptualized considering all regulatory , complaince and legal constraints . There is continuous check on regulatory and complaince framework that the product adhers to . There is a seprate legal & complaince team which notifies the product of any developments in this space and also approves any changes going to production.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 43, 'ONE', 'Organization conducts initial and subsequent surveys for product/feature usage post which the feature is deprecated and  also send an email update to the regular users regarding removal of the feature with sufficient notice period before removing the same.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 43, 'TWO', 'Key Objectives ,Regulatory adherence and MoS  are mapped to the feature / product. Every year we monitor its traction points  and any major -ve impact on baseline ( Key objective, MoS) determines a call on feature deprecation. Post this an email update to the regular users is sent regarding removal of the feature with sufficient notice period before removing the same.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 43, 'FOUR', 'Key Objectives ,Regulatory adherence and MoS  are mapped to the feature / product and we do a constant monitoring of its traction . Any major -ve impact on baseline ( Keyo bjective , measure of sucess )  determines a call on feature deprecation/ fix.Performance impact of that feature along with load changes if any are observed post which an email update to the regular users is sent regarding removal of the feature with sufficient notice period before removing the same.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 43, 'FIVE', 'Key Objectives ,Regulatory adherence and MoS  are mapped to the feature / product and we do a constant monitoring of its traction. Features always have a running hypothesis with them and any changes observed in the same determine feature utility. Once we determine feature utility is on decreasing trend , performance impact of that feature along with load changes if any are observed and impact assesment is nullified, feature deprecation timeline is decided and the information is sent to customers via various communication channels. The regular users are updated with sufficient notice period before removing the feature.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 44, 'ONE', 'There is no concept of version sunset and all product versions existing are supported.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 44, 'TWO', 'The product supports and maintains all versions since launch and we generally do sunset only when the last of the customer is moved to a  higher version. Its purely the customer decision. The product is in growth stage and minimal influence on customers to migrate them. ( even if the cost of maintaining it outweighs the AMC revenue from the customer).');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 44, 'FOUR', 'The product supports and maintains all versions since launch . We generally identify the version that should be sunset  based on various parameters (cost/no of customers/outdated technology/resources etc) and we continously urge customers to move to a higher/latest version (give cheaper option/Value prop/benefits/ or even do it for free ). But do sunset only when the last of the customer is moved to a  higher version. ( even if the cost of maintaining it outweighs the AMC revenue from the customer).');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 44, 'FIVE', 'The product supports only the versions on new platform/architecture which are very few and also backward compatible. The design ensures zero or minimal impact on upgrade making it easy for customers to confidently upgrade.product versions on the older architecture are sunset at phased intervals giving ample time to customers to migrate to the new platform. A clear migration path /strategy is planned and defined for the customers to ensure smoother transition. where the customer is not interested to move, minimal or no support is provided for any changes/bugs reported based on the cost benefit analysis. The product is in matured stage and can influence the customers to upgrade.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 45, 'ONE', 'Key Objectives ,Regulatory adherence are mapped to the initiative/ product. However traction points  ( Product analytics points / usage / new customers ) aren''t captured.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 45, 'TWO', 'Key Objectives ,Regulatory adherence and MoS are mapped to the Initiative / product , MoS mapping with traction points is done however  monitoring isn''t done.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 45, 'THREE', 'Key Objectives ,Regulatory adherence and MoS  are mapped to the feature / product , MoS mapping with traction points monitoring is done on yearly basis.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 45, 'FOUR', 'Key Objectives ,Regulatory adherence and  MoS  are mapped to the feature / product , MoS mapping with traction points is done and monitoring as well update ( MoS mapping with traction points )  is done on a regular basis');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 45, 'FIVE', 'Key Objectives ,Regulatory adherence and mesasure of sucess  are MoS  are mapped to the feature / product , MoS mapping with traction points is done and monitoring as well update ( MoS mapping with traction points )  is done on a regular basis. Industry specific disruptive trends are also looked at and impact assessment done on a quarterly basis.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 46, 'ONE', 'There is no systematic user research on continuous basis and is usually done as an adhoc exercise outsourced to a third party organisation skilled in doing the same, based on feedback from internal/external customers (CSAT Score). Customer Experience is often interchangeably used with user experience and hence the product team has minimal or no focus on it.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 46, 'THREE', 'The organisation understands the importance of user research and has a dedicated user experience team. However the team has minimal or no understanding of the expected outcomes due to lack of collaboration with required stakeholders on a regular basis.  The research is done based on adhoc requests and  does not provide a clear insight /understanding of the personas/ user empathy maps and how they bring in value.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 46, 'FIVE', 'The user research team collaborates with the product team on a continuous basis and does systematic and extensive research  and clearly identifies personas and their empathy relationships and relating them into customer segments and value the product brings to the customer segments.(Equity --- Value to the clearly identified customer segments ). The teams work together in identifying user journeys and works to create an impactful user and customer experience.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 47, 'ONE', 'No experience modeling , No user research is done.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 47, 'TWO', 'Experience modeling such as Customer Journey/Service Blueprint is created but only based on intuitive thinking rather than user research. Customer segmentation is not done.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 47, 'THREE', 'Experience modeling such as Customer Journey /Service Blueprint is done only when initiating the product. It continuously refined, evolved. But customer segmentation dynamics are not added to the curve.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 47, 'FOUR', 'Experience modeling is used in various stages of product development. The experience model is updated considering the customer segmentation dynamics. Only some individuals can interpret the changes and insights of the modeling results. Discussion with other dependent stakeholders are not done.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 47, 'FIVE', 'Experience modeling is used in various stages of product development. The experience model is updated considering the customer segmentation dynamics. Most individuals can interpret the changes and insights of the modeling results. Discussion happen regularly with current product support and consumer support teams  and the competitors'' behavior  and the consumer adoption towards them is constantly observed and assessed.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 48, 'ONE', 'No Experience research practices, Ux/Ui is developed and approved by the Product Owner /Manager in conjunction with high-level stakeholders');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 48, 'TWO', 'Experience Practice occurs but with the pseudo customer ( Usability testing in some ways)');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 48, 'THREE', 'Experience research practices occur with a very small sample size of beta customers but for few selected product initiatives.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 48, 'FOUR', 'Qualitative & Quantitative experience practices occur regularly but with relatively with smaller sample size. ( Sample size is beta users at best and experience lab session isn''t pre determined but on actual , session doesnt account for spikes based on market conditions)');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 48, 'FIVE', 'Qualitative & Quantitative experience practices occur regularly both at the interactive prototype and before the actual launch.( Sample size is pre detrmined and experience lab session is pre determined and accounts for spikes based on market conditions)');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 49, 'ONE', 'Identification of stakeholders is incidental.As and when during the process of product inception to delivery, the needed stakeholders are identified and brought in to review/provide feedback. The stakeholders do not have complete context and hence the feedback provided is not complete and accurate. There is no clear understanding of the stakes of each stakeholder and their role in the product success.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 49, 'THREE', 'The stakeholders and their stakes are clearly identified upfront.  However there is no well defined process for stakeholder collaboration or communication resulting in lack of shared understanding on the product at various stages.The product showcase usually happens at the end and there is no/minimal opportunities for incorporating feedback.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 49, 'FIVE', 'The stakeholders and their stakes are clearly identified upfront. There is well defined process of collaboration and communication that provides a uniform understanding to stakeholders at all times.The product showcase is planned at regular intervals (not to be confused with sprint showcase ) and the feedback from the stakeholders are captured and prioritized and incorporated where applicable. ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 50, 'ONE', 'No routine communication and collaboration across teams where there are dependencies. Knowledge resides in team owner''s minds and is not captured or shared across teams. Dependencies identification is accidental and usually ends up in a blame game.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 50, 'TWO', 'The dependencies to a large extent are identified upfront during the planning and the interdependent teams meets where needed. However the action points are not tracked in a proper format leading to misplaced information, delays and blame game. Shared knowledge exists but the information is low in quality or difficult to access.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 50, 'THREE', 'The dependencies to a large extent are identified upfront during the planning and the interdependent teams meets at regular intervals .Shared knowledge exists but does not identify the owner/accountability for any action being taken on identified interdependencies. Experience and lessons are captured for use within own team or related teams, however, most of them does not know this exists or cannot easily access it.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 50, 'FOUR', 'The dependencies to a large extent are identified upfront during the planning and the interdependent teams meets at regular intervals. Each interdependency has owner and the actions are detailed and clearly calls out the requirements/data.  Cross-team meetings are regular and centered around reporting the teams progress and informing others. Synchronization points are maintained separately. However failure scenario is never predetermined and is a knee-jerk reaction.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 50, 'FIVE', 'The interdependent team meets at regular intervals they have a predetermined communication format hence articulation of interdependencies is very clear and precise.Each interdependency has owner and the actions are detailed and clearly calls out the requirements/data. It also has a predetermined agenda for the next meeting. The dependencies to be delivered/dropped etc are clearly documented with a valid reasoning. Cross-team meetings are regular and also interested in engaging and resolving disputes and providing feedback.Failure scenarios are also discussed and well documented. Upstream stakeholders are kept aware of the progress.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 51, 'ONE', 'No routine communication and collaboration across departments at all.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 51, 'TWO', 'Departments aren''t having a predetermined meeting. Intra Departmental meetings occur only if they have identified some future dependencies');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 51, 'THREE', 'Routine Departmental meetings. No communication format hence departments cannot articulate their interdependencies and the synchronization points. shared knowledge exists but the information is low quality or difficult to access.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 51, 'FOUR', 'Routine Departmental meetings, other than status reporting, departmental meetings interested in engaging and resolving disputes and providing feedback. .Shared Knowledge exists is informative and detailed identifies the owner and also identified the action and actionable data. It also has a predetermined agenda for the next meeting and action points to be discussed in the next meeting. Also has an archive of sections of things that have been dropped off and the reason for the dropping.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 51, 'FIVE', 'Routine Departmental meetings, other than status reporting, departmental meetings interested in engaging and resolving disputes and providing feedback. The interdepartmental team exists Budget cuts & manpower constraints are also within the purview of Intra departmental team. Shared Knowledge exists is informative and detailed identifies the owner and also identified the action and actionable data. It also has a pre-determined agenda for the next meeting and action points to be discussed in the next meeting. Also has an archive of sections of things that have been dropped off and the reason for the dropping.');

-- INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
-- VALUES ( 73, 'ONE', '');
-- INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
-- VALUES ( 73, 'TWO', '');
-- INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
-- VALUES ( 73, 'THREE', '');
-- INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
-- VALUES ( 73, 'FOUR', '');
-- INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
-- VALUES ( 73, 'FIVE', '');
