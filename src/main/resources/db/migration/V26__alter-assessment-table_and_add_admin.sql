INSERT INTO tbl_access_control VALUES ('sagarp@thoughtworks.com','Admin');

ALTER TABLE tbl_assessment
    DROP COLUMN IF EXISTS assessment_purpose;

ALTER TABLE tbl_assessment
    ADD COLUMN assessment_purpose varchar(100) DEFAULT 'Just Exploring' CHECK ( assessment_purpose = 'Internal Assessment' OR
                                                                                assessment_purpose = 'Client Assessment' OR assessment_purpose = 'Just Exploring');
