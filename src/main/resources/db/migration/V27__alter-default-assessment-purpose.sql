ALTER TABLE tbl_assessment ALTER COLUMN assessment_purpose SET DEFAULT 'Internal Request';
UPDATE tbl_assessment SET assessment_purpose = 'Internal Request';