CREATE TABLE tbl_user_assessment_modules(
      id SERIAL PRIMARY KEY,
      assessment INT REFERENCES tbl_assessment(assessment_id) NOT NULL ,
      module INT REFERENCES tbm_assessment_module(module_id)  NOT NULL
)