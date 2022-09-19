CREATE TABLE tbl_user_assessment_modules(
      assessment INT REFERENCES tbl_assessment(assessment_id) NOT NULL ,
      module INT REFERENCES tbm_assessment_module(module_id) NOT NULL , CONSTRAINT assessment_module UNIQUE (assessment,module),
      primary key (assessment,module)
)