CREATE TABLE tbl_assessment_modules(
      assessment INT REFERENCES tbl_assessment(assessment_id) NOT NULL ,
      module INT REFERENCES tbm_assessment_module(module_id) NOT NULL , CONSTRAINT assessment_module UNIQUE (assessment,module),
      primary key (assessment,module)
)
INSERT INTO tbl_access_control VALUES ('prachi.agarwal1@thoughtworks.com','Admin');
