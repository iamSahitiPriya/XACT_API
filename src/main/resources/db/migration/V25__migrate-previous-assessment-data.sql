INSERT INTO tbl_assessment_modules(assessment, module)
SELECT assessment.assessment_id, modules.module_id
FROM tbl_assessment assessment, tbm_assessment_module modules
WHERE modules.is_active = true AND modules.category IN (SELECT category_id FROM tbm_assessment_category WHERE is_active = true);