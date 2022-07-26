INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (6,'Operational efficiency');

INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (14, 'Product & platform stability', 6);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (15, 'People, process & tools', 6);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (16, 'Infrastructure Stability', 6);

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (30, 'Availability', 14);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (31, 'Success indicators',14 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (32, 'Infrastructure',14 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (33, 'Quality', 14 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (34, 'Incident management',15 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (35, 'Observability',15 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (36, 'Infrastructure',15);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (37, 'Quality', 15);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (38, 'Culture', 15);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (39, 'Availability',16 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (40, 'Success indicators',16 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (41, 'Infrastructure',16 );
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (42, 'Observability', 16);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 91,'Operations downtime', 30);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 92,'Performance', 30);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 93,'Metrics', 31);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 94,'Performance', 31);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 95,'Hosting', 32);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 96,'Application Stability', 33);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 97,'Incident reporting and tracking', 34);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 98,'Alerting', 35);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 99,'Logging', 35);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 100,'Monitoring', 35);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 101,'Accessibility', 36);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 102,'Compliance', 36);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 103,'Delivery', 36);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 104,'Hosting', 36);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 105,'Maintenance', 36);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 106,'Resilience', 37);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 107,'Security', 37);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 108,'Execution', 38);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 109,'Capacity',39 );
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 110,'Performance',39 );
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 111,'Toil',39 );
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 112,'Metrics',40 );
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 113,'Toil',40 );
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 114,'Deployment',41 );
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 115,'Monitoring',42);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 116,'Tracing',42 );

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (124, 'What is the max time in which the application can load without any impact to business? If it is delayed, what would be the impact?', 91);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (125, 'Production incidents in a <time period>? Is there any pattern observed to indicated certain spikes in incidents ( eg. Cyber Sunday)', 91);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (126, 'What is the freq. of down time? Is there any pattern observed to indicated certain spikes in down-time ( eg. Cyber Sunday)', 91);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (127, 'Currently, what is the mechanism in place for DR and BCP. How often are the mock drills run', 91);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (128, 'How frequently requests are failing over the last 6 months? what is the data captured for it? What remediation steps are in place in case of a failure', 91);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (129, 'How frequently do your services get saturated? what is the data captured for it?  What remediation steps are in place in case of a failure', 91);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (130, 'What is your products current failover mechanism?', 92);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (131, 'What is the current resiliency for availability', 92);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (132, 'What is the current resiliency for performance issues', 92);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (133, 'Is the service live, in Production and being used by end users? For how long it has been live?', 92);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (134, 'Do you forecast growth of key business indicators in the future? How far into the future you forecast that growth (months, years)', 93);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (135, 'Do you measure forecast accuracy by comparing the forecast to the observed values over the measurement period? What is the observed percent error for the1 year forecast? (0-100+)', 93);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (136, 'What are the current platform/product metrics that are being tracked and their data over the last 1 year? Are they being benchmarked?', 93);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (137, 'What is the deployment freq over the last 1 year? Is it being measured and tracked?', 93);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (138, 'What is the lead time for changes for the last 1 year? Is it being measured and tracked?', 93);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (139, '"What are the availability and performance objectives for this product?"', 94);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (140, 'What is the average latency and what is an average target latency for the user? Can you share the data for the last 1 year?', 94);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (141, 'What is the current perf. threshold? Is it measured and tracked?', 94);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (142, 'Is there a mechanism in place which can cater to increased demands in terms of scalability', 95);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (143, 'What is the current state of IAC in the platform/product? Is there any IAC implemented which ensure ease in extisibility and modularity', 95);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (144, 'Are there any measures in place for Data consistency at a given point in time? Are inconsistencies being tracked? What is the remediation plan?', 96);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (145, 'Do you have measurement of tech debt? What is the current prioritisation strategy for tech debt?', 96);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (146, 'Do you have target for resolving your tech debt and what is the timeline?  How frequently is tech debt resolved or remidiated? ', 96);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (147, 'Incidents of data loss or recovery over the last 1 year', 96);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (148, 'What kind of support for users is in place today', 97);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (149, 'Are your routine processes documented? E.g. if you told a new team member to go upgrade the version of Linux on all your VMs, would they be able to find and follow instructions on how to do this?', 97);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (150, 'Can you share data on what %age of tickets are being closed at first responder level? E.g., are the incidents resolved without escalating to subject matter experts / co-workers?', 97);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (151, 'Of the requests that need intervention from support team, what %age needed change request?', 97);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (152, 'What kind of RCA process for incidents is in place and what are the timelines for it?', 97);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (153, 'What is the current notification mechanism?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (154, 'Who is responsible for L1 issues - do you actively carry a pager/ any notification device with you?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (155, 'What is the current escalation matrix for any alerts? Is business involved in defining the matrix? How frequently it is revisited?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (156, 'What is incident response process in place? Can you share data on it? Do you have playbooks around incident response?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (157, 'How long does it take for an incident to reach the right team?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (158, 'Is there an alert for every key metric?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (159, 'Are all alerts defined by good, signal-providing thresholds?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (160, 'Are alert thresholds set appropriately so that alerts will fire before an outage occurs?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (161, 'Are all alerts actionable?', 98);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (162, 'Who has access to the logs that are being generated. How frequently are these access reviewed?', 99);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (163, 'What is the current logging level? What is the configurability of the logging levels if you want to change it?', 99);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (164, 'What is the current retention period of any diagnostic information?', 99);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (166, 'How many services are monitored? If some are not montiored, why are they left unmonitored?', 100);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (167, 'Have you build connectors/webhooks to get information from different monitoring tools? If yes, which are the tools that are interconnected and how frequently do they get the data?', 100);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (168, 'What are the specific domains that you need Obs/Monitoring for (Payments, Accounting, Profiling etc)', 100);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (169, 'What are the authentication/authorisation mechanisms that are being followed?', 101);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (170, 'What are the regulatory compliances applicable for your geographic region?  E.g., do your servers have to be in a specific Cloud zone or region?', 102);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (171, 'What are the regulatory compliances applicable for your industry? ', 102);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (172, 'Do you maintain your CI/CD tool or it is outsourced?', 103);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (173, 'Are there quality gates for continuous deployment', 103);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (174, 'How is responsibility shared between customer & TW infra teams in the scope of provisioning & maintenance?', 104);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (175, 'Do you have any Regulatory compliance for your hosting? Do you know what constraints determine the location of your service? Do your serving, processing, and data storage have to be colocated, or in the same zone or region? Do you have data regionalization requirements?', 104);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (176, 'Is TW responsible for scanning for CVEs and patching cycles?', 105);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (177, 'What is the current QA strategy. What kind of test suites do you have in place', 106);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (178, 'Are their any measurements around COPQ(cost of poor code quality) level? If yes, can you share the data', 106);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (179, 'Is there a pattern on the incoming defects? For eg. on the application front or the infrastructure front? What is the process for defect remediation?', 106);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (180, 'What is the current defect leakage?', 106);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (181, 'Are you doing chaos engineering? If yes, can you share the current process and simulations in place?', 106);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (182, 'Have they done threat modelling? If yes, can you share data/artefacts around it?', 107);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (183, 'How are vulnerabilities tracked and remediated? Are there any policies around existing vulnerabilities remediation?', 107);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (184, 'Do you monitor security aspects of the application? If yes, how?', 107);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (185, 'Do you do runtime security scans? if yes - do they run in enforcement mode?', 107);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (186, 'What are the security implementation at logging level', 107);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (187, 'How decentralised are your teams in terms of decision making, product functionality etc.', 108);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (188, 'How does your team(s) approach resource management and requirement changes', 109);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (189, 'What is the current process for acquiring more resources? Is it well defined and documented?', 109);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (190, 'Do you know what constraints determine the location of your service? E.g., do your servers have to be in a specific Cloud zone or region?', 109);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (191, 'Do your serving, processing, and data storage have to be colocated, or in the same zone or region? Do you have data regionalization requirements?', 109);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (192, 'Level of demand being placed on the system over the last 1 year?', 110);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (193, 'Time taken to service a request? Are there any SLAs in place and are they being tracked? What has been the average time taken over the last 1 year?', 110);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (194, 'How quickly can artefacts get generated and deployed?', 111);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (195, 'What are the key indicators of health of infrastructure? Can you share the data for the last 1 year?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (196, 'What is the current response time?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (197, 'What is the current system throughput?  How many concurrent users can be supported? What has been the pattern over the last 1 year?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (198, 'What is the error budget', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (199, 'What is the durability level?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (200, 'What is SLI SLO SLA for individual service? How it’s defined ? How it’s devided (as per feature or as per project)', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (201, 'What is current MTTI? What has been the pattern over the last 1 year?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (202, 'What is current MTTD? What has been the pattern over the last 1 year?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (203, 'What is current MTTR? What has been the pattern over the last 1 year?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (204, 'What is the current change Failure Rate? What has been the pattern over the last 1 year?', 112);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (205, 'List down different places where you see TOIL is currently present?', 113);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (206, 'What are the deployment strategies that are currently in place For Eg.Is there a canary phase in the deployment pipeline?', 114);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (207, 'Do deployments run in the canary phase for a period of time that is long enough to catch any failures?', 114);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (208, 'Are deployments to production done all at the same time, or incrementally rolled out? Why is the current strategy being followed?', 114);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (209, 'Is there a procedure in place for skipping the staging and canary phases in case of an emergency?', 114);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (210, 'Are there  any mechanisms in place for backups, alternatives, fallbacks, or defensive caching? If yes, what are the mechanisms?', 114);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (211, 'Is there a centralised dashboard to monitor infrastructure and applications?', 115);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (212, 'What is the retention period for metrics? Any compliance requirements for storing the data?', 115);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (213, 'How do you approach real-time monitoring of the systems?', 115);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (214, 'Are the metrics collected being used for any data analytics / business inference?', 115);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (215, 'Who is responsible for monitoring of your application?', 115);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (216, 'What is monitoring architecture for critical services ? Is there any high availability (HA) setup in place ?', 115);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (217, 'How is tracing is happening in the current application architecture? For How long the traces are stored? Any compliance requirements for storing the data (eg. banking regulations might ask you to store them for longer)', 116);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (218, 'How frequently traces are used in incident management', 116);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (219, 'What is total time spent on TOIL ( Mundane, repetitive, automate able tasks)', 113);

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 30, 'ONE', 'No historical trend on the downtime data. High Priority incidents are frequent along with very frequent downtimes. No historical trend in the peak usage data patterns. DR and BCP not in vogue. No failover mechanisms in place. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 30, 'TWO', 'Data on downtime and peak usage is only for a quarter. SLAs are breached often. High priority incidents happen often, but not frequently.  Failover mechanisms are in place but not measured for effectiveness. Overall resiliency of the application is measured but not as a norm or a continuous process. DR and BCP drills are in place, but not a norm.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 30, 'THREE', 'All the resiliency data is available and tracked for least 2-3 quarters and benchmarked. High priority incidents are less frequent and are manged with in the SLAs. Peak usage patterns are measured. Failover mechanisms in place. DR and BCP plans and drills are in place and followed.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 30, 'FOUR', 'Sensible defaults. Resiliency data is measured and kept track of and used to continuously make changes to the systems to adapt to growing requirements. Load balancing, fail over mechanisms are in place and effective to handle peak usage patterns. DR and BCP plans are very efficiently followed at  set intervals.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 30, 'FIVE', 'Everything in 4 are constantly revisited and realigned based on business needs. Chaos engineering practises, AIOPs tools are in place. ');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 31, 'ONE', 'No units defined, or units defined but no historical trends.
No forecasting done on the key business indicators. No data being tracked on the product/platform metrics. No tracking on the deployment frequency, lead time for changes, availability and performance of the application. No clear objectives on the availability and performance defined. Latency is not measured. Performance thresholds not defined ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 31, 'TWO', 'Defined business units, historical trend for units, but limited ability for forecasting these units. Forecasts are either inaccurate or short-term (<=1 quarter)
Data on latency, performance, overall availability is defined but not tracked beyond a quarter. Deployment frequency is in place but not effectively followed, more adhoc still. Lead time to changes is again measured but not tracked over a longer period of time');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 31, 'THREE', 'Have solid historical trend, have a forecast for 4-6 quarters. Have a non-trivial forecast model.
Data on latency, performance, overall availability is defined and measured over a year or more. Deployment frequency is in place and followed. Lead time to changes is measured and tracked over a year or more. Performance thresholds are measured and tracked regularly.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 31, 'FOUR', 'Have measured the accuracy of past predictions. Using 6-8 quarter forecasts.
All data related to availability and performance are very efficiently tracked. Threshold are established and constant adaptations are in place to ensure that the measured data is met and improved upon.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 31, 'FIVE', 'Defined & measured & accurate forecast, know how to forecast accurately & improving record of accuracy; accuracy of actual demand within 5% of 6-month forecast.
Ongoing evaluation and refinement all parameters on a regular basis, possibly using AI-Ops methodologies');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 32, 'ONE', 'No clear multi-cloud, hybrid , on prem or cloud strategy for the next 3-4 years. Any issues related to infrastructure take a lot of time to get addressed. Infrastructure issues are frequent and are of high priority. Multiple points of failure. No IAC in place.Resources are deployed by hand in an ad hoc fashion. Relationships between system components are undocumented. No idea if resources deployed are sufficient.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 32, 'TWO', 'Infrastructure issues happen often. Single point of failures are identified. Cloud or hybrid staretgy is in placeRules of thumb exist for service constraints (database and web near each other) but constraints are not measured (unverified). Defined process for acquiring resources; but process is manual.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 32, 'THREE', 'IAC is in place, but scattered. Documentations are in place. The team is moving completely towards cloud.Acquiring resources is mostly automated. When to acquire resources is a manual process.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 32, 'FOUR', 'Sensible defaults are in place. Cloud adoption and usage is high. Comprehensive IAC is in place. Canary deployment pipelines are in place. Rollback strategies are in placeSystem constraints are modelled, but lack evaluation. Service churn over time can cause constraints in the model to drift from reality. When to acquire resources is driven by forecasting.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 32, 'FIVE', 'Everything in 4 are constantly revisited and realigned based on business needs.Models are continuously evaluated and maintained even against service churn. Where to acquire capacity is driven by models, when to acquire capacity is driven by forecasting.');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 33, 'ONE', 'No data on the measurements regarding Data consistency at a given point in time. No data on security vulnerabilities and data loss incidents. No indication of Tech Debt in the systems and no target for resolving the tech debt. Data inconsistency is high');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 33, 'TWO', 'Vulnerabilities are assessed, but effective remediation plan is not in place.Tech debt is tracked but no plan in place for its reduction. Data inconsistency incidents are frequent and tracked only for a period of quarter');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 33, 'THREE', 'High priority Vulnerabilities and Data inconsistency issues are tracked. However, there is some lack in prioritising them and addressing them. Tech Debt reduction strategy is in place.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 33, 'FOUR', 'Vulnerability remediation plan is in place and followed judiciously. Tech Debt is paid off frequently. Teams to do not have practise of Tech Debt sprints. Data inconsistency is tracked and remediated on priority without major business impact');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 33, 'FIVE', 'Security scanners are integrated into the pipeline. Chaos Engineering simulations are run from time to time.Tech Debt is handled with the understanding that the more successful product is, the higher tech debt it carries.Tech Debt is paid off whenever touching the codebase');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 34, 'ONE', 'Ad hoc support: best effort, daytime only, no actual rotation, manual alerting, no defined escalation process. No followup or recognition of systemic error, no root cause, outages contained but not analyzed, the same incident keeps happening, long term trends are not recognized.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 34, 'TWO', 'Documented rotation and response time. Automated alerts integrated with monitoring. Training is ad hoc. Defined postmortem process with action items. Followup on action items is poor (only P0 items addressed.) Root cause analysis (RCA) insufficient (not enough Whys). Similar incidents recur due to failure of action item followup or poor RCA.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 34, 'THREE', 'Training process exists (wheel of misfortune, shadow oncall, etc.) Rotation is fully staffed but some alerts require escalation to senior team members to resolve. Postmortem process is applied to all major incidents (w/action items). Action items at P0 level are prioritized. RCA is extensive and correctly attributes root cause in the majority of incidents.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 34, 'FOUR', 'Measurement of MTTR and MTTD for incidents. Handoff between rotations. Most incidents require minimal escalation (can be handled by on-call alone). Postmortems come with annotated metadata to facilitate analysis. Postmortems are shared widely amongst affected teams to learn from mistakes.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 34, 'FIVE', 'Weekly review of incidents and refinement of strategy, handoffs, communication between shifts, majority of problems resolved without escalation, review value and size of rotation, evaluating scope. Incident response protocol established. All action items closed in a timely manner, reviewed by involved teams and others for learning purposes, identify problem areas, process in place to make sure action items are completed, standardized format, review of the postmortem process to see if it provides value.');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 35, 'ONE', 'No Dashboards. Data gathering is ad hoc, inconsistent, and undocumented. No logging/tracing mechanisms in place. Monitoring and alerting dont exist. Escalation matrix and Incident Response Plan or a playbook not in place. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 35, 'TWO', 'Dashboards may exist for key metrics, but only in static form (no customization). Dashboards not centrally located, or dashboard use is not standardized. Dashboard ownership is unclear. Logging and tracing are implemented but not very effectively. Monitoring and alerting mechanism are not in place. Escalation matrix and IRP (Incident Response Plan) are in place but very formally followed.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 35, 'THREE', 'Dashboards exist and support common technical use cases. Dashboard ownership is clear. Tools to support ad hoc queries exist but are somewhat cumbersome (using them requires training or hours of experimentation to produce a result.). Logging and tracing are well established and gather the right kind of data. Monitoring and alerting mechanisms are in place but at a very rudimentary level and do not cover the entire spectrum of services. IRP and Escalation matrix are in place and followed');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 35, 'FOUR', 'Dashboards support both technical and business use cases. Ad hoc data exploration tools are customizable and support common use cases without training (e.g. by following a recipe) or through intuitive interfaces. Logging and tracing are a norm and well established. Right kind of data gathered and retained for a defined period to be able to do Root Cause Analysis on issues. Monitoring and alerting mechanisms are setup efficiently and cover at least 80% of all the services. Threshold are defined well in the monitoring tools to ensure true alerting and avoid false alerts.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 35, 'FIVE', 'Dashboards are standardized across the business units. Everything in 4 plus advanced mechanisms of AI-Ops are in vogue to ensure proper monitoring and alerting mechanisms. Logging and tracing covers all the services in place. Very efficient IRP and Escalation matrix process in place.');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 36, 'ONE', 'No Devops practises in place. No clear multi-cloud, hybrid , on prem or cloud strategy for the next 3-4 years. Pipelines break frequently. Deployment is done adhoc. Any issues related to infrastructure take a lot of time to get addressed. Infrastructure issues are frequent and are of high priority. Multiple points of failure. Resources are deployed by hand in an ad hoc fashion. Relationships between system components are undocumented. No idea if resources deployed are sufficient. No proper compliance in place. No proper authentication/authorization mechanisms in vogue');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 36, 'TWO', 'DevOps maturity is low. DevOps teams are in place just to manage pipelines and deployments. Deployments are planned, but schedules and timelines are not stringent. Infrastructure issues happen often. Single point of failures are identified. Cloud or hybrid strategy is in place Rules of thumb exist for service constraints (database and web near each other) but constraints are not measured (unverified). Defined process for acquiring resources; but process is manual. Proper authentication mechanisms in place but not authorization. Compliances are still not efficiently established');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 36, 'THREE', 'DevOps is active in pipeline and infrastructure management. Deployments are well planned. Canary Deployment is in a strategy teams are being thought about. IAC is in place, but scattered. Documentations are in place. The team is moving completely towards cloud. Acquiring resources is mostly automated. When to acquire resources is a manual process. Regulatory compliances are very efficiently adhered to. All authentication and authorization mechanisms are in place. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 36, 'FOUR', 'Sensible defaults are in place. Cloud adoption and usage is high. Comprehensive IAC is in place. Canary deployment pipelines are in place. Rollback strategies are in place System constraints are modelled, but lack evaluation. Service churn over time can cause constraints in the model to drift from reality. When to acquire resources is driven by forecasting. Regulatory compliances, authentication/authorization are all defaults and well established');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 36, 'FIVE', 'Everything in 4 are constantly revisited and realigned based on business needs. Models are continuously evaluated and maintained even against service churn. Where to acquire capacity is driven by models, when to acquire capacity is driven by forecasting.');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 37, 'ONE', 'No proper defect remediation in place. Unit Testing, No integration Testing. Manual testing is prevalent. No data on security vulnerabilities No indication of Tech Debt in the systems. Data inconsistency is high.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 37, 'TWO', 'Defect remediation plan is present, but not effectively followed. Vulnerabilities are assessed, but effective remediation plan. Unit tests are present. No proper integration test suite. Tech debt is tracked but no plan in place for its reduction. Data inconsistency incidents are frequent. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 37, 'THREE', ' Unit test and Integration test are present. Test suite not integrated with the pipeline Test coverage is between 50-60%. High priority Vulnerabilities and defects are prioritised and remediated based on priority level. Tech Debt reduction strategy is in place. Data inconsistency is tracked and remediated');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 37, 'FOUR', 'Automated test coverage suite integrated in the pipeline with test coverage is between 60-80%. Security practises are in place and security scans are done regularly. Vulnerability remediation plan is in place and followed judiciously. Tech Debt is paid off frequently. Teams to do not have practise of Tech Debt sprints. Data inconsistency is tracked and remediated on priority without major business impact');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 37, 'FIVE', 'Security scanners are integrated into the pipeline. Chaos Engineering simulations are run from time to time. Test coverage is above 80%. Tech Debt is handled with the understanding that the more successful product is, the higher tech debt it carries.Tech Debt is paid off whenever touching the codebase');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 38, 'ONE', 'No clear separation of responsibilities between the teams');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 38, 'TWO', 'Team level boundaries on their ownership exist but not very formally followed. Still presence of grey areas');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 38, 'THREE', 'Clearly separated responsibilities across the product teams with a clear line of leadership');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 38, 'FOUR', 'Well established, de-centralised teams which have clearly defined responsibility boundaries and there are no grey areas ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 38, 'FIVE', 'Everything in 4 plus that the teams are completely decentralised and are highly autonomous in their functioning');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 39, 'ONE', 'No units defined, or units defined but no historical trends. Acquiring new resources is a very adhoc process. No understanding or forecasting of the resource management or requirement changes. Application artefacts are manually generated and deployed everytime after a downtime');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 39, 'TWO', 'Defined business units, historical trend for units, but limited ability for forecasting these units. Forecasts are either inaccurate or short-term (<=1 quarter) Service has defined SLOs. Service has measurement of SLO but it is ad hoc or undocumented. Forecasting of resource requirements in place but not effective and inaccurate. SLAs are in place but frequently breached. Additional resource requirements are managed but no formal process in place. Application artefacts generation is mostly manual');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 39, 'THREE', 'Have solid historical trend, have a forecast for 4-6 quarters. Have a non-trivial forecast model. Service has defined SLOs. Measurement is a documented process with clear owner. SLAs are in place and followed with rigour with some slippages far and between. Additional resource requirements are forecasted accurately and is a well established process. Application artefact generation and restoration is well automated. Understanding of the increasing load on the system over a period of a year or more');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 39, 'FOUR', 'Have measured the accuracy of past predictions. Using 6-8 quarter forecasts. SLO is published to users. SLO is measured automatically. SLO represents user requirements. Measurements are well established. SLAs are every efficiently followed. Resource requirements are well forecasted and prepared for. Acquiring is automated in many ways. System demand over the past year is well documented. Creation and restoration of artefacts are automated and very efficient. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 39, 'FIVE', 'Defined & measured & accurate forecast, know how to forecast accurately & improving record of accuracy; accuracy of actual demand within 5% of 6-month forecast. Ongoing evaluation and refinement of SLO with users. Resource needs are forecasted very efficiently and mechanisms for auto scaling up and down are in place. Self healing mechanisms are in place to ensure auto application artefacts are auto generated and restored');


-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 40, 'ONE', 'No units defined, or units defined but no historical trends.
Service has no SLO or SLO does not represent user requirements. TOIL is predominant across the board');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 40, 'TWO', 'Defined business units, historical trend for units, but limited ability for forecasting these units. Forecasts are either inaccurate or short-term (<=1 quarter)
Service has defined SLOs. Service has measurement of SLO but it is ad hoc or undocumented. TOIL is addressed albeit at a small scale. There still is a significant portion that is manual. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 40, 'THREE', 'Have solid historical trend, have a forecast for 4-6 quarters. Have a non-trivial forecast model.
Service has defined SLOs. Measurement is a documented process with clear owner. TOIL is controlled and are constantly addressed. However, not on a complete scale. Lot that are in scope for automation');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 40, 'FOUR', 'Have measured the accuracy of past predictions. Using 6-8 quarter forecasts.
SLO is published to users. SLO is measured automatically. SLO represents user requirements. Error budgets are defined and followed. TOIL is pretty much non-existent. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 40, 'FIVE', 'Defined & measured & accurate forecast, know how to forecast accurately & improving record of accuracy; accuracy of actual demand within 5% of 6-month forecast.
Ongoing evaluation and refinement of SLO with users. Well defined Error budgets in place. Advanced AI-Ops mechanisms in vogue to eliminate toil completely.');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 41, 'ONE', 'No Devops practises in place. No clear multi-cloud, hybrid , on prem or cloud strategy for the next 3-4 years. Pipelines break frequently. Deployment is done adhoc. Any issues related to infrastructure take a lot of time to get addressed. Infrastructure issues are frequent and are of high priority. Multiple points of failure.
Resources are deployed by hand in an ad hoc fashion. Relationships between system components are undocumented. No idea if resources deployed are sufficient. No Backup mechanisms in place');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 41, 'TWO', 'DevOps maturity is low. DevOps teams are in place just to manage pipelines and deployments. Deployments are planned, but schedules and timelines are not stringent. Infrastructure issues happen often. Single point of failures are identified. Cloud or hybrid strategy is in place
Rules of thumb exist for service constraints (database and web near each other) but constraints are not measured (unverified). Defined process for acquiring resources; but process is manual. Backup mechanisms in place but not very efficiently managed. Data retained only for a few days');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 41, 'THREE', 'DevOps is active in pipeline and infrastructure management. Deployments are well planned. Canary Deployment is in a strategy teams are being thought about. IAC is in place, but scattered. Documentations are in place. The team is moving completely towards cloud.
Acquiring resources is mostly automated. When to acquire resources is a manual process. Backup mechanisms in place and data rention is for around a month and very well established restoration process as well');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 41, 'FOUR', 'Sensible defaults are in place. Cloud adoption and usage is high. Comprehensive IAC is in place. Canary deployment pipelines are in place. Rollback strategies are in place
System constraints are modelled, but lack evaluation. Service churn over time can cause constraints in the model to drift from reality. When to acquire resources is driven by forecasting. Backup mechanisms in place, with proper retition time of data being arrived at based on the changes going in. Properly established data restoration mechanisms in place as well');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 41, 'FIVE', 'Everything in 4 are constantly revisited and realigned based on business needs.
Models are continuously evaluated and maintained even against service churn. Where to
acquire capacity is driven by models, when to acquire capacity is driven by forecasting. AI models are employed to understand and establish data backup and restoration mechanisms ');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 42, 'ONE', 'No Dashboards. Data gathering is ad hoc, inconsistent, and undocumented. No Real time monitoring and tracing is non existent');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 42, 'TWO', 'Dashboards may exist for key metrics, but only in static form (no customization). Dashboards not centrally located, or dashboard use is not standardized. Dashboard ownership is unclear. Not very efficient in terms of coverage of the services in real time monitoring dashboard. Tracing is very rudimentary at best');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 42, 'THREE', 'Dashboards exist and support common technical use cases. Dashboard ownership is clear. Tools to support ad hoc queries exist but are somewhat cumbersome (using them requires training or hours of experimentation to produce a result.). Real time monitoring is in place but covers only around 50% of the existing services. HA setup is not in place. Tracing mechanisms are in place and constantly tweaked based on need');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 42, 'FOUR', 'Dashboards support both technical and business use cases. Ad hoc data exploration tools are customizable and support common use cases without training (e.g. by following a recipe) or through intuitive interfaces. Real time monitoring covers around 80% of the services in place and proper alerting mechanisms are setup alongside. Tracing is very effective and used frequently for debugging and RCAs. HA is in place');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 42, 'FIVE', 'Dashboards are standardized across the business units. Everything in 4 plus very efficient HA setup in place. 100% of the services are covered under real time monitoring with effective visualisations of the data. ');

-------------





