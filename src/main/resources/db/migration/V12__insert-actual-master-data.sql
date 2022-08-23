INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (7,'Cloud platform');

INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (17, 'Cloud Operating Model', 7);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (18, 'Cloud readiness', 7);

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (43, 'Team structures, roles and responsibilities', 17);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (44, 'Skills and training programs', 17);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (45, 'Process', 17);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (46, 'Org readiness', 18);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (47, 'Readiness and compatibility of applications', 18);

INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 117,'Cloud CoE, strategy and adoption team structure', 43);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 118,'Roles and responsibilities ', 43);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 119,'Cloud migration team expertise', 43);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 120,'Capability building', 44);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 121,'Hiring', 44);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 122,'Operating processes', 45);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 123,'Exec and Management Support', 46);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 124,'Tech Readiness', 46);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 125,'Regulatory Compliance', 46);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 126,'Discovery and Assessment', 47);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 127,'Application/workload readiness (optional - applicable only in the context of specific workload)', 47);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 128,'DevOps', 47);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 129,'Observability & Monitoring', 47);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 130,'Disaster Recovery and High Availability', 47);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
VALUES ( 131,'Security', 47);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (220, 'Do you have a Cloud CoE established-  to define, architect and guide your cloud strategy org-wide?', 117);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (221, 'Have you started the efforts to standardize and automate commonly needed platform components and tooling i.e. do you have sensible defaults for tech stack and platform components?', 117);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (222, 'Which of the following teams are in place / identified?

1. Cloud adoption / execution team - to implement cloud migration and or modernization for various identified applications?
2. Cloud governance team - to define and govern key KPIs measurement around security, compliance, and cost management
3. Cloud operation team - to monitor your application on cloud', 117);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (223, 'Do you have key stakeholders identified for each of the above structure?', 117);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (224, 'Do you have key key point of contacts / lead identified for -

1) capability and skill building
2) cloud execution champion
3) cloud governance
4) budget and cost management etc. key functions', 117);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (225, 'Cloud Solution Architect role', 118);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (226, 'Cloud SME', 118);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (227, 'Cloud Infrastructure Admin/ DevOps roles', 118);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (228, 'SRE / Cloud Support roles', 119);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (229, 'Does your team have cloud migration expertise or prior experience?', 119);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (230, 'Has the team explored any tools to assist during workload assessment and cloud migration process?', 119);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (231, 'Has the team created any pre-migration checklist?', 119);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (232, 'What are the success criteria for your cloud migration team?', 119);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (233, 'Have you identified core skill sets needed for 1) cloud-native engineering 2) cloud migration  and re-platform  3) SRE 4) cloud security 5) Infra and devops ', 120);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (234, 'What programs exist for skilling / capability building to upskill your current workforce in these tech stacks?', 120);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (235, 'What cloud-specific training resources are available to employees?', 120);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (236, 'How do you track and drive the completion of training goals?', 120);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (237, 'How does your organization support individuals in getting certifications offered by your cloud provider(s)?', 120);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (238, 'What is the timeline of the skilling program and how it aligns with your cloud migration execution timeline?', 120);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (239, 'What is the level of agile maturity of your teams? Have you planned for their agile training if required?', 120);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (240, 'Do you have hiring needs clearly identified in your team staffing plans?', 121);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (241, 'Do you have budget allocated for hiring?', 121);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (242, 'Have you identified key roles to hire across many team specified above?', 121);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (243, 'Do you have clear job descriptions available to hire at various levels?', 121);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (244, 'Do you have skilled people to hire right talent?', 121);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (245, 'What is your hiring timeline and does it align with your migration execution timeline?', 121);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (246, 'Have you defined operating processes around following aspects of your cloud environment ?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (247, 'How is your organization structure reflected in cloud subscriptions or accounts?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (248, 'How the access control will be managed at subscription or account level?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (249, 'How is the cloud financial model defined in terms of - chargeback or funding of shared resources, budgeting, reporting etc.', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (250, 'Do you have a strategy and plan around designing your landing zones?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (251, 'Have you identified a plan for resource rightsizing, dynamic scaling, automated env. provisioning and de-provisioning (lower level env. management for cost saving)?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (252, 'Have you identified policies for cloud spend limits?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (253, 'How will license management be done?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (254, 'How will the monitoring, alerts and notification be set up? Who will get what level of alerts and notification?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (255, 'Are high-level compliance and security requirements identified?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (256, 'What communications and change management plans are in place?', 122);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (257, 'What KPIs are defined and how will they be tracked?', 122);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (258, 'Does the cloud strategy have an exec sponsor?', 123);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (259, 'HHave you identified the key stakeholders required for the success of org-wide cloud adoption (stakeholders can be categorized in 4 buckets: 1) exec stakeholders and key influencers 2) Major management stakeholders and champions 3) Key technical decision makers 4) executors)', 123);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (260, 'What are the key motivations behind your cloud migration strategy (not mutually exclusive ):
1. data center exit
2. M&A, divestiture
3. Business agility
4. Reduce capital expenses
5. reduce carbon footprint
6. New regulatory compliances changes
7. New data sovereignty requirements
8. Fixing current scalability and resilience issues', 123);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (261, 'Is your cloud migration strategy aligned with the business objectives of the organization?', 123);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (262, 'Has your company identified a specific business reason for moving to the cloud?', 123);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (263, 'Do all the business units and functions understand the need for the cloud strategy?', 123);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (264, 'What are the key business outcomes and goals for your cloud migration strategy?', 123);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (265, 'Have you conducted an org-wide communication program for plan and need for cloud strategy?', 123);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (266, 'In general, what is your approach towards build vs. buy? do you have a strategy in place for which applications to build from scratch vs. buy in form of COTS?', 124);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (267, 'Have you done any analysis of COTS / SaaS offerings that can solve your business problems before building the applications on cloud?', 124);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (268, 'Has your Cloud CoE or enterprise architecture group established the sensible defaults for platform components and tooling?', 124);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (269, 'Are there any concerns regarding Cloud vendor lock-in due to usage of cloud native managed services?', 124);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (270, 'Have you done a high-level analysis of tech skills required for your cloud journey and current availability of those skills in the organization?', 124);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (271, 'Are there any privacy concerns to move the data to the cloud?', 125);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (272, 'What are the data residency compliances to be followed?', 125);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (273, 'What are all the key regulatory compliances (i.e. GDPR, CCPA, etc.) and regulations that the organization is  liable to follow during the cloud migration and modernization journey?', 125);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (274, 'Have you identified and classified sensitive data requirements to be considered before cloud data migration?', 125);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (275, 'Have you identified the set of applications in scope for the migration project?', 126);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (276, 'Have you performed an application/workload discovery and assessment using a tool ?', 126);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (277, 'Do you have clear understanding of chosen applications and their dependencies (upstream and downstream)?', 126);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (278, 'What approaches to modernization and innovation have been pursued before? What was the outcome?', 126);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (279, 'Are the application owners identified and aligned to the migration plan?', 126);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (280, 'Have you identified any toolchain for your Continuous integration and deployment (CI/CD)?', 126);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (281, 'What is the business criticality of the application to be moved to the cloud?', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (282, 'Do we have the cross-border control to move the data to the cloud?', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (283, 'Do you have Data residency compliance requirements?', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (284, 'What is the tech stack of the app? ', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (285, 'What are the external integrations', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (286, 'Does your App has any middleware / networking dependency? ', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (287, 'What are the upstream and downstream dependencies ', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (288, 'Detail out the specific storage requirements for the application? Do you have requirement for NoSQL or SQL data store? How is your current data estate looks like?', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (289, 'Detail out the networking requirements for the application? Do you have requirement for external API integration? ', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (290, 'Did you identified any automated tool to gather the storage / networking  / infra requirements?', 127);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (291, 'Detail out the aspects of securing your workloads? (i) identity & access (ii) app/data security (iii) network security (iv) threat protection', 127);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (292, 'How comprehensive is your CI/CD setup?
- which CI/CD toolchain do you use? How is the CI/CD pipeline set up end-to-end?
- which IaaC framework do you use?
- How many environments are setup for path to production?', 128);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (293, 'Do you have any requirements to run build agents (CI/CD) on on-premise?', 128);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (294, 'What Application Performance metrics are you monitoring or would like to monitor in Cloud?', 129);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (295, 'What is your current observability and application monitoring strategy? What toolchain are you using?', 129);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (296, 'Are you planning to use on-premise tool chain for observability and monitoring or you are open for Cloud native Monitoring tools?', 129);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (297, 'How are the issues classified as P1/P2? Are the SOPs established for priority and severity of issues?', 129);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (298, 'How the SLAs are defined for issue resolution?', 129);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (299, 'Does your application(s) have ability to health-check the dependencies?', 129);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (300, 'Do you have Data residency compliance requirements?', 130);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (301, 'Do you have the plan for Business continuity , backup and disaster recovery for the migration project?', 130);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (302, 'Does you application have High Availability requirements? Shall it be deployed in multiple availability zone or different geographical location?', 130);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (303, 'How is your current DR strategy?', 130);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (304, 'What is your Recovery Point Objective (RPO) and Recovery time Objective (RTO) for the cloud service you plan to migrate?', 130);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (305, 'What, if any, contractual obligations does the organization have to fulfill regarding Service Level Availability and Service Resiliency to its customers (both internal and external)?', 130);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (306, 'How does the organization ensure that applications are compliant with security and regulatory policies? Does this change in the context of applications being deployed in the cloud instead of on-premises?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (307, 'Has the organization suffered any information or security breaches recently that should be considered?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (308, 'Is your organization subject to any regulation regarding placement/storage of certain types/classifications of data in the cloud?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (309, 'Is your organization subject to any auditing/reporting requirements regarding applications/data stored in the cloud?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (310, 'All the workloads are properly analysed using automated tools and their future state is identified. Workload disposition map is created based on a structured framework. For the re-architect option, the target cloud architecture is created at hig-level which used the underlying cloud offering. This entire exercise is clearly communicated and agreed. Migration program is in alignment with long term business vision. ', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (311, 'Is your organization subject to any regulations regarding data sovereignty?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (312, 'How does your organization currently, or intend to, segment/segregate data stored in the cloud?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (313, 'Does your organization intend to use cloud provider authored encryption keys (ie. KMS), or use the organization’s own/independent keys?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (314, 'What existing security hardware/software solutions does your organization intend to bring to/into the cloud?', 131);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (315, 'Is your organization planning to use or already using any kind of Threat Modelling tools? ', 131);

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 117, 'ONE', 'Each department is exploring cloud on their own , no CoE structure, on-need basis work done by architects');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 117, 'TWO', 'Each department has their own CoE working on cloud strategy. Adoption and governance handled on ad-hoc basis. Cloud operations handled by separate team. Various such silos exist in organization');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 117, 'THREE', 'Each department has their own CoE working on cloud strategy. Knowledge sharing on-need basis for cloud adoption and governance strategy. Cloud operations being run by central cloud ops teams. An attempt to break the silos');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 117, 'FOUR', 'Central cloud CoE team laying down the foundation for sensible defaults, architecture principle. Cloud Adoption and governance is org-wide initiative carefully planned, departments are dependant on central CoE.  Cloud operations being run by central shared cloud ops teams.  Cloud standardization and unification - a key agenda');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 117, 'FIVE', 'Departments identify and implementation their cloud initiatives. Central cloud CoE provides support, training, governance strategy, sensible defaults, knowledge from other parts of the organization. Cloud Adoption and governance is federated at dept level but in alignment with central CoE. Cloud operations run as in federated manner. ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 118, 'ONE', 'Havent thought about what roles are needed to support cloud adoption, still early stage');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 118, 'TWO', 'Have idea about the role definition, JD but need to plan number, budget and approvals etc.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 118, 'THREE', 'Have the roles, number and team structure identified, have earmarked few internal team members. Some of them are to be upskilled. But no training programs in place');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 118, 'FOUR', 'Have the roles, number and team structure identified, have earmarked few internal team members. Some of them are to be upskilled. Training programs in place. External hiring number is also approved');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 118, 'FIVE', 'Have the roles, number and team structure identified, have earmarked few internal team members. Some of them are to be upskilled. Training programs in place. External hiring is in progress, some experts have already joined.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 119, 'ONE', 'No expertise in team');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 119, 'TWO', 'Few people may have some expertise, not taken stock of yet.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 119, 'THREE', 'The team has prior knowledge and experience');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 119, 'FOUR', 'The team has prior knowledge and experience, and has carried out some POCs');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 119, 'FIVE', 'The team has done successful migrations to production environments in the past. Has identified the tools to assist at all phases in migration.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 120, 'ONE', 'Don''t have a concrete capability building plan in place. Teams are doing this exercise on need basis. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 120, 'TWO', 'Have done capability identification based on past experience, but not considering future needs. Have some pieces of training plans in place. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 120, 'THREE', 'Capability identification and maturity are identified. However, training enablement is in progress');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 120, 'FOUR', 'Capability maturity and learning paths are identified. Training enablement is in place but tracking effectiveness and individual progress etc. are not fully functional.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 120, 'FIVE', 'Clear capability maturity and learning paths exist for each role. Associated training needs are identified. Individuals tracking process in place. Effectiveness of training is measured on periodic basis.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 121, 'ONE', 'Ad-hoc / in progress staffing plan exists but hiring needs are not flagged clearly');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 121, 'TWO', 'Staffing plan along with key roles identified for hiring is in place. Budget approval in progress');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 121, 'THREE', 'Staffing plan along with key roles identified for hiring is in place. Budget is approved. JD available for some roles, hiring team still figuring out JDs');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 121, 'FOUR', 'A staffing plan along with key roles identified for hiring is in place. JDs are clearly defined. Hiring teams are onboarded with the various needs and JDs and hiring activity is yet to start. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 121, 'FIVE', 'A staffing plan along with key roles identified for hiring is in place. JDs are clearly defined. Hiring teams are onboarded with the various needs and JDs and hiring activity is already going on and aligned with migration timelines.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 122, 'ONE', 'Haven''t thought holistically about these topics yet, still very early in cloud journey and need help');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 122, 'TWO', 'The importance of some areas is identified, and some partial processes are in place. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 122, 'THREE', 'Processes are identified incrementally, but they are not reviewed and agreed upon.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 122, 'FOUR', 'SOPs (standard operating procedures) and RACI matrix are defined. Some processes are already in execution. A communication and change management plan is yet to be in place.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 122, 'FIVE', 'Detailed SOPs and RACI matrix are defined. It is well communicated across teams and everyone is aware. Policies and execution are already in place. Any change in processes is handled and communicated by Cloud CoE with a well-defined protocol and SOP');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 123, 'ONE', 'Organization have no business strategy and budget forecasted for moving to the cloud. They just have an initial idea of moving to cloud.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 123, 'TWO', 'Organization have preliminary strategy for move to cloud but lack sponsorship. They need to create business case and present to stakeholder for getting sponsorship. They are working on vision , goal.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 123, 'THREE', 'Organization have clear strategy to move to cloud and aware of cloud benefits. Management have willingness to sponsor but are looking for strong business case.  ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 123, 'FOUR', 'Organization have sponsorship for moving to cloud and identified business strategy for the same. They are aware of the benefit of moving to cloud but have no detailed cloud adoption plan laid out for moving to cloud.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 123, 'FIVE', 'Organization have clear strategy and business sponsorship for moving to cloud. They have laid out high level plan for both cloud migration and DC exit. They have solid long term roadmap  defined for key business objectives . Cloud migration strategy and business vision are well aligned.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 124, 'ONE', 'Organization have no tech strategy and experience of moving any workload to cloud.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 124, 'TWO', 'Organization have idea and aware of tech stack required for moving to cloud . They lack experience in moving any workload to cloud. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 124, 'THREE', 'The organization has some experience in migrating the workload to the cloud predominantly using Lift and shift. They lack experience in cloud-native application development and hybrid cloud. Upskilling program is identified for the dev team.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 124, 'FOUR', 'The organization has good experience in migrating complex workloads in the lift and shift model. They have fair experience in hybrid cloud strategy and cloud-native application development. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 124, 'FIVE', 'The organization have solid experience in cloud migration projects. They have successfully executed many migration projects to the cloud with automation for repeated tasks. They are well versed with different types of migration projects such as Rehost, Re Platform, Re-Architect .');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 125, 'ONE', 'Have not thought of any compliance and regulatory requirements yet while moving to the cloud. This has not been planned properly. Compliance procedures are adhoc and hardly followed.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 125, 'TWO', 'Organizations have partially planned to address regulatory compliance and security requirements while working on migration projects. But they lacks detailed process and methodology and do not have qualified experts to perform the sign-off. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 125, 'THREE', 'Organization are aware of regulatory and compliance requirements. They have the dedicated team to certify , audit the compliance and security requirements. They lack the process and execution while auditing and implementing the requirements. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 125, 'FOUR', 'Organization have established regulatory and compliance process requirements , auditing  , certification. Data classification is done. They have executed the same in multiple projects.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 125, 'FIVE', 'Organization have established regulatory and compliance process requirements , auditing  , certification and data classification. They have executed the same in multiple projects including the Cloud migration projects. The process is well designed and defined for anyone to follow. They have regular audit cadence planned and executed. ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 126, 'ONE', 'No dedicated efforts to understand the need and benefits of the migration. Adhoc approach by various teams - randomly picking apps for migration as experiments on cloud.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 126, 'TWO', 'Workloads are assessed manually in an isolation without thinking of a big picture and planned to be replatformed/rehosted in cloud. Assessment approach is not documented and reinvented every time.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 126, 'THREE', 'Workload assessment approach is documented and reused by the team to some extent, but it is still manual. Some efforts are being put in organizing this into a program. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 126, 'FOUR', 'Workload assessment approach is standardized across teams. Rehost and replatform workloads are identified. However, analysis of cloud services and target state cloud architecture is not in place yet.  Migration program is being viewed and prioritized at org level.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 126, 'FIVE', 'All the workloads are properly analysed using automated tools and their future state is identified. Workload disposition map is created based on a structured framework. For the re-architect option, the target cloud architecture is created at high-level which used the underlying cloud offering. This entire exercise is clearly communicated and agreed. Migration program is in alignment with long term business vision. ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 127, 'ONE', 'Bulky apps without any specifications about upstream and downstream dependencies ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 127, 'TWO', 'Dependencies are known and documented. Team knows the networking, data residency requirements for cloud movement');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 127, 'THREE', 'Application structure, modules, maturity etc. are not assessed for cloud migration yet. Migration benefits are still not clearly identified');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 127, 'FOUR', 'Assessment for the replatforming on cloud is done. Required re-engineering changes for cloud migration are identified. Test strategy is identified for Replatforming work. Migration Benefits are known');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 127, 'FIVE', 'Cloud migration pre work is done with automated tools. Dependencies (if any) from onprem to cloud are known and feasible ');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 128, 'ONE', 'manual build and deployment by each teams');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 128, 'TWO', 'manual build and deployment by central team');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 128, 'THREE', 'CI/CD without quality checks');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 128, 'FOUR', 'CI/CD with quality checks');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 128, 'FIVE', 'application templates based cosinstent CI/CD implementation');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 129, 'ONE', 'Support teams are reacting to prod issues. No dashboards available for monitoring. Typically these issues are reported by the internal or external users. No SOPs are defined to handle these issues.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 129, 'TWO', 'Support teams are referring to some dashboards to understand the health of the system. It takes some personal experience/expertise to interpret these dashboards and identify an issue. Logs are not easily accessible while troubleshooting');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 129, 'THREE', 'Dashboards are efficient enough to clearly identify the issues. Few of the issues are notified to support teams but few have to be identified using dashboards. Logs can''t be fetched efficiently.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 129, 'FOUR', 'Teams are having notification configured based on issue severity and SOPs are identified for some of the issue. Logs can be fetched without any system induced slowness.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 129, 'FIVE', 'Services are equipped with health-checks to proactively detect the potential issue. Support teams are notified with a system generated notification when issue arises. All these issues are already classified based on their severity and team has SOPs handy to troubleshoot the issues within agreed SLA.');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 130, 'ONE', 'Application is running on a single data center/cloud region. Stakeholders are not thinking of DR strategy');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 130, 'TWO', 'Application is running on a single data center or region but stakeholders have defined DRO and tech solution is underway');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 130, 'THREE', 'In case of disaster, team will manually spin up the new instances. Downtime due to the failure/disaster is unpredictable and not agreed');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 130, 'FOUR', 'Automated pipeline is configured to spin up an application instance in the new data center or new region in case of failure. Downtime needed is already agreed across the org');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 130, 'FIVE', 'DRO has goal to keep the application up without any downtime caused by infra. Application (including DB) is replicated across multiple region with real time data sync');

INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 131, 'ONE', 'Organization treats security as afterthought and no security practices are followed.');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 131, 'TWO', 'Organization have security practices partially documented and structured. Organization does not have a dedicated team. Adhoc guidance available to validate security practices and provide recommendation. Not all the projects goes through same security rigor and practices. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 131, 'THREE', 'Organization have security practices well documented and have dedicated security lead to  take care of process and tools, .But security practices are not enforced and adhoc process is followed. ');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 131, 'FOUR', 'Organization have security practices well documented and have dedicated security lead to  take care of process and tools, . every team has identified security champion who works with central security lead to enforce practices');
INSERT INTO tbm_assessment_param_reference(parameter, rating, reference)
VALUES ( 131, 'FIVE', 'Every team and their security champion works closely with central security lead to plan proactive threat modelling exercises. Security audits are planned and executed periodically.');

SELECT setval('category_seq',(select max(category_id)+1 from tbm_assessment_category),false);
SELECT setval('module_seq',(select max(module_id)+1 from tbm_assessment_module),false);
SELECT setval('topic_seq',(select max(topic_id)+1 from tbm_assessment_topic),false);
SELECT setval('parameter_seq',(select max(parameter_id)+1 from tbm_assessment_parameter),false);
SELECT setval('question_seq',(select max(question_id)+1 from tbm_assessment_question),false);


