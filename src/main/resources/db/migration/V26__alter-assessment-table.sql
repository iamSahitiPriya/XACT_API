ALTER TABLE tbl_assessment
    ADD COLUMN assessment_purpose varchar(100) DEFAULT 'Client Request' CHECK ( assessment_purpose = 'Internal Request' OR
                                                       assessment_purpose = 'Client Request');