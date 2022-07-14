INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (1, 'Software engineering');
INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (2, 'Product and Design');
INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (3, 'Cloud platform');
INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (4, 'Data platform');
INSERT INTO tbm_assessment_category(category_id, category_name)
VALUES (5, 'Org design');
--------------------------------------------------------------------

INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (1, 'Architecture Quality', 1);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (2, 'DevOps', 1);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (3, 'Quality Assurance', 1);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (4, 'Project Management', 2);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (5, 'Product Management', 2);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (6, 'ProductOps', 2);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (7, 'Cloud Modernization', 3);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (8, 'Data readiness', 4);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (9, 'Data architecture', 4);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (10, 'Digital readiness', 5);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (11, 'Capability and culture', 5);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (12, 'Regulation readiness', 5);
INSERT INTO tbm_assessment_module(module_id, module_name, category)
VALUES (13, 'Vendor optimisation', 5);
--------------------------------------------------------------------

DELETE
FROM tbm_assessment_topic;
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (1, 'Architectural style', 1, 'Topic');
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (2, 'API Strategy', 1, 'Parameter');
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (3, 'Technology Stack', 1, 'Topic');
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (4, 'Performance', 1, 'Parameter');
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (5, 'Governance', 1, 'Topic');


-----------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (6, 'Product vision evolution & strategy', 5, 'Parameter');
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (7, 'OKR`s - Organization & Products', 5, 'Parameter');
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module, assessment_level)
VALUES (8, 'Go To market strategy', 5, 'Topic');
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (9, 'Product analytics ', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (10, 'Intangibles (Product Management Skills and Focus)', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (11, 'Product Launch & Launch capability', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (12, 'Gather Feedback , Feedback loop & bucketing of feedback', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (13, 'Innovation & Thought Leadership', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (14, 'Product Delivery', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (15, 'Product Deprecation & Sunset', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (16, 'Customer Experience', 5);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (17, 'Stakeholder Management', 5);

------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (18, 'Continuous Integration and Deployment', 2);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (19, 'Production operations', 2);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (20, 'Environments', 2);

-------------------

INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (21, 'Cloud Modernization', 7);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (22, 'Security and Compliance', 7);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (23, 'Operations', 7);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (24, 'Legal and Finance', 7);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (25, 'Applications Strategy', 7);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (26, 'Product teams R&R and Processes', 6);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (27, 'Product Adoption ', 6);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (28, 'Streamlining Product Execution', 6);
INSERT INTO tbm_assessment_topic(topic_id, topic_name, module)
VALUES (29, 'Streamlining Communication ', 6);


--------------------------------------------------------------------------


INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 1,'Components', 1);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 2,'Dependencies', 1);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 3,'Data model', 1);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 4,'Standards', 2);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 5,'API basic hygiene', 2);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 6,'API best practices', 2);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 7,'Technology Stack', 3);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 8,'Resilience', 4);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 9,'Scalability', 4);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 10,'Availability', 4);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 11,'Architecture decisions', 5);
INSERT INTO tbm_assessment_parameter(parameter_id,parameter_name, topic)
        VALUES ( 12,'Conformance', 5);

INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (13, 'Product Evolution Methodology ', 6);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (14, 'Business / Revenue Model ', 6);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (15, 'Product Strategy ', 6);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (16, 'Product Positioning ', 6);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (17, 'Measures of sucess & their evolution || MOS ~ OKR', 7);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (18, 'GTM - crafting a well defined strategy', 8);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (19, 'Product Analytics /Metrics  ', 9);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (20, 'Core Attributes / Skills that should be there as a part of Product Management role.  ', 10);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (21, 'Infuence of Poduct Management ', 10);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (22, 'PM aspect -- Why , When & Where ', 10);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (23, 'Stratergy Focus ', 10);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (24, 'Strategic Depth', 10);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (25, 'Strategic Focus at Inter-Team Leavel', 10);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (26, 'Launch Capability ', 11);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (27, 'Launch selection ', 11);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (28, 'Feedback Loop Cycle ', 12);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (29, 'Thought leadership', 13);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (30, 'Product Innovation process', 13);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (31, 'Trend Identification & Impact assesment', 13);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (32, 'Product Backlog', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (33, 'Product Backlog Management', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (34, 'EPICS Writing', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (35, 'Epics Value measurement', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (36, 'User Stories', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (37, 'Story value measurement', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (38, 'Story qualit management', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (39, 'Client Customizations & Extensions ( SAAS Based Products)', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (40, 'Client Customizations & Extensions ( Onpremise Based Products)', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (41, 'Feature Priortization Methodology', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (42, 'Product Alignment ', 14);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (43, 'Unship Features (deprecate redundant features) vs Feature sunset', 15);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (44, 'Version Sunset', 15);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (45, 'Trigger points', 15);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (46, 'CX and UX (R and D)', 16);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (47, 'Market & User Models', 16);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (48, 'Experience Lab (User experience)', 16);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (49, 'Stakeholder Management (Inception to Delivery)', 17);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (50, 'Intra team communication  ', 17);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (51, 'Departmental Dependency Management', 17);

---------------

INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (52, 'Build', 18);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (53, 'Deployment', 18);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (54, 'Observability', 19);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (55, 'Business Continuity', 19);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (56, 'Provisioning and use', 20);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (57, 'Best practices', 20);

---------------

INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (58, 'Cloud Strategy', 21);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (59, 'Disaster Recovery', 21);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (60, 'Security Strategy', 22);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (61, 'Disaster Recovery', 22);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (62, 'Operational Strategy', 23);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (63, 'Legal', 24);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (64, 'Finance', 24);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (65, 'Application Architecture', 25);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (66, 'Application Resiliency', 25);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (67, 'Application Performance', 25);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (68, 'Culture', 25);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (69, 'Additional', 25);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (70, 'Product Ops Structure & Composition', 26);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (71, 'Process Streamlining (Internal)', 26);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (72, 'Managing product stack and tools', 26);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (73, 'Internal Team On-boarding', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (74, 'Client On-boarding', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (75, 'Develop a continuous education program', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (76, 'Make the right data easily available', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (77, 'Acting On Data', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (78, 'Client Doc Collaborations', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (79, 'Competition analysis collaborations', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (80, 'Product feedbak management', 27);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (81, 'Address Emerging Issues (Production Issues / Operation issues )', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (82, 'Managing product Uptime', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (83, 'Experimentation execution', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (84, 'Experimentation Management', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (85, 'Roll out Industry best practices', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (86, 'Regulatory', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (87, 'Establish & Streamline critical and routine tasks and processes', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (88, 'Vendor On-boarding', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (89, 'Vendor Management', 28);
INSERT INTO tbm_assessment_parameter(parameter_id, parameter_name, topic)
VALUES (90, 'Work with other team', 29);








-------------------------------------------------------------------------------


INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (1, 'How do components map to the domain at hand?', 1);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (2, 'How do components interact with each-other?', 1);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (3, 'To what extent modularity is ensured at low-level implementation ?', 1);

INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (4, 'What are the internal and external system dependencies?', 2);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (5, 'Do components have their own data model, or how do they map to the underlying data model?', 3);
INSERT INTO tbm_assessment_question(question_id, question_text, parameter)
VALUES (6, 'Can you give an overview of the underlying data model?', 3);


---------------------------------------------------------------------------------

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (1, 'ONE',
        'Big ball of mud monolith. Dependencies are Ad-hoc, little to no separation between core and third-party application logic. Changes in third-party integration logic usually requires major changes in a majority of systems, including user interfaces.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (1, 'TWO',
        'Structured Monolith. Separation between core and third-party components exists. However, changes to third-party components usually results in major changes to service components directly interfacing with these third party components.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (1, 'THREE',
        'Service Oriented Microservices. Separation between core and third-party components exists. Logic is cleanly veiled off in third party components. However, there is little clarity of non-functional resilience characteristics.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (1, 'FOUR',
        'Event-Driven Microservices and service-oriented microservices are used as applicable. Separation between core and third-party components exists. Logic and non-functional requirements are cleanly veiled off in third party components. However, each team uses its own set of implementations when integrating with third parties.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (1, 'FIVE',
        'Serverless Functions, Event-Driven Microservices and service-oriented microservices are used as applicable. Standardized integration templates exist and are actively used. These standardized templates are designed with specific care being taken to isolate third party logic and resilience expectations.');

----------

INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (2, 'ONE',
        'API gateway is not used as an explicit pattern. No explicit strategy is used for API versioning. API Consumers are notified if backwards incompatible changes are made and are expected. API documentation is largely missing or ignored. API clients are required to consult producers'' source code to look for usage patterns. No explicit strategy is used for API versioning. API Consumers are notified if backwards incompatible changes are made and are expected. API documentation is largely missing or ignored. API clients are required to consult producers'' source code to look for usage patterns.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (2, 'TWO',
        'The use of API gateway is explicitly discouraged to avoid it becoming a home of excessive business logic. API versioning is used very sparingly. In most cases, changes are made in a backwards compatible manner. When backwards incompatible changes are required to be made, new APIs are created. API documentation is produced at the start of a project, but is not maintained up-to-date. Hence is used sparingly by consuming teams.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (2, 'THREE',
        'API Gateway is used as a single entry point for all consumers. It acts as a simple proxy for all downstream services and as a switch to enable zero downtime deployments. URL based API versioning is used at all times. Consumers are expected to include a version identifier in the request at all times. API documentation is produced and all efforts are made to keep it up-to-date with with implementation changes. However, documentation does experience drift from implementation because it is maintained separately from the implementation.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (2, 'FOUR',
        'API Gateway is used as a single entry point for all consumers. It is used to expose different variations of the same API to different consumers and as a switch to enable zero downtime deployments. Header-based API versioning is used at all times. Consumers are expected to include a version identifier in the request at all times. API documentation is produced and kept in sync with implementation at all times. Documentation and implementation are produced from the same source repository.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES (2, 'FIVE',
        'API Gateway is used as a single entry point for all consumers for value added services such as authentication, authorization and resilience patterns such as circuit breaker, rate limiting, throttling etc. Header-based API versioning is used when multiple versions of an API need to co-exist. Consumers not including an explicit version identifier are returned responses from the latest version. API documentation and examples are produced and kept in sync with implementation at all times. Documentation is generated from tests and published as an artifact of the build. All documentation is published on a central API management portal for easy discoverability and access.');

------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 3, 'ONE', 'Architecture predominantly uses a technology stack that is unsupported, extremely difficult to extend or out-of-date.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 3, 'TWO', 'The technology stack has less support, either needs the goodwill of the open source community or needs involves expensive licenses. It is possible to extend the architecture but at the expense of business priorities and downtime');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 3, 'THREE', 'Technology stack is not proprietary but deprecated or around 5 years out-of-date with the current way of doing things. ');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 3, 'FOUR', 'Technology stack is current, but not quite the best fit for the problem domain. For eg; data stores use data models that have been force-fitted to capture the domain model');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 3, 'FIVE', 'Technology stack is state-of-the-art and architecture is open to extend. The problem domain nicely fits in with the tech stack being used.');

-------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 5, 'ONE', 'Architecture decisions are made on an ad-hoc basis with individual teams responsible for all major decisions. There is little to no record maintained for decisions made. The primary means to enforce architecture decisions is through documents and accompanying literature.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 5, 'TWO', 'Architecture decisions are made by a central committee outside of the team and pushed to teams for adoption. The primary means to enforce architecture decisions is through architecture and code reviews.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 5, 'THREE', 'Architecture decisions are initiated by teams working on a problem. These decisions are reviewed with a central committee to ensure standards are adhered to. Architecture decisions are enforced through a combination of documentation, reviews and static analysis observations.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 5, 'FOUR', 'Architecture decisions are initiated by teams working on a problem. These decisions are reviewed with a central committee to ensure standards are adhered to. Decisions made are captured and persisted in a lightweight manner. Architecture decisions are enforced through a combination of documentation, reviews static analysis observations. In addition, some architecture decisions are enforced using a set of fitness functions that are integrated as part of the build.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 5, 'FIVE', 'A pattern catalog of historic architecture decisions is consulted prior to large projects/undertakings. Teams works with an embedded representative from an architecture forum to expedite decisions from both a domain and technology perspective. These decisions are reviewed with a central committee to ensure standards are adhered to. Decisions made are captured and persisted in a lightweight manner. Architecture decisions are enforced through a combination of documentation, reviews static analysis observations. In addition, a majority of architecture decisions are enforced using a set of fitness functions that are integrated as part of the build.');

---------------
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 4, 'ONE', 'Resilience patterns like circuit breakers, timeouts, service discovery, rate limiting, throttling and caching are not used. Performance and scale testing are not practiced.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 4, 'TWO', 'Resilience patterns like circuit breakers, timeouts, service discovery, rate limiting, throttling and caching are used sporadically by individual teams. Performance and scale testing are not practiced, but only on an ad-hoc basis.');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 4, 'THREE', 'Resilience patterns like circuit breakers, timeouts, service discovery, rate limiting, throttling and caching are documented, with varying implementation styles. Performance and scale testing are not practiced, but only before significant milestones (like a large cross-functional release)');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 4, 'FOUR', 'There are standardized implementation across teams on Resilience patterns like circuit breakers, timeouts, service discovery, rate limiting, throttling and caching. Performance and scale testing are practiced periodically, but not part of the pipeline');
INSERT INTO tbm_assessment_topic_reference(topic, rating, reference)
VALUES ( 4, 'FIVE', 'Implementation of resilience patterns like circuit breakers, timeouts, service discovery, rate limiting, throttling and caching are standardized and tested to meet SLAs. Performance and scale testing are practiced consistently and automated as part of the pipeline');
