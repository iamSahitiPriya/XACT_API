/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

ALTER TABLE tbm_assessment_category ADD COLUMN "is_active" BOOLEAN DEFAULT TRUE;
ALTER TABLE tbm_assessment_module ADD COLUMN "is_active" BOOLEAN DEFAULT TRUE;

UPDATE tbm_assessment_category SET is_active = false WHERE category_id=3;
UPDATE tbm_assessment_category SET is_active = false WHERE category_id=4;
UPDATE tbm_assessment_category SET is_active = false WHERE category_id=5;


UPDATE tbm_assessment_module SET is_active = false WHERE module_id=3;
UPDATE tbm_assessment_module SET is_active = false WHERE module_id=4;
