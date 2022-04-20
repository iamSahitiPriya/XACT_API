
-- create sequence
CREATE SEQUENCE organisation_id_seq;

-- set the current value of the sequence to the max value from that column
-- (id column in this scenario)
SELECT SETVAL('organisation_id_seq', (select max(organisation_id) from tbl_organisation), false);

-- use sequence for the target column
ALTER TABLE tbl_organisation ALTER COLUMN organisation_id SET DEFAULT nextval('organisation_id_seq');


-- create sequence
CREATE SEQUENCE assessment_id_seq;

-- set the current value of the sequence to the max value from that column
-- (id column in this scenario)
SELECT SETVAL('assessment_id_seq', (select max(assessment_id) from tbl_assessment), false);

-- use sequence for the target column
ALTER TABLE tbl_assessment ALTER COLUMN assessment_id SET DEFAULT nextval('assessment_id_seq');

ALTER TABLE tbl_assessment DROP COLUMN description;

ALTER TABLE tbl_assessment DROP COLUMN assessment_status;

ALTER TABLE tbl_assessment ADD COLUMN assessment_status char(20) CHECK(assessment_status='ACTIVE' OR assessment_status='COMPLETED');



