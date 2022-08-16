ALTER TABLE tbm_assessment_parameter ADD COLUMN "is_active" BOOLEAN DEFAULT TRUE;
ALTER TABLE tbm_assessment_topic ADD COLUMN "is_active" BOOLEAN DEFAULT TRUE;

ALTER TABLE tbm_assessment_category ADD COLUMN "comments" text;
ALTER TABLE tbm_assessment_module ADD COLUMN "comments" text;
ALTER TABLE tbm_assessment_parameter ADD COLUMN "comments" text;
ALTER TABLE tbm_assessment_topic ADD COLUMN "comments" text;

ALTER TABLE tbm_assessment_category ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_module ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_parameter ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_topic ADD COLUMN "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tbm_assessment_category ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_module ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_parameter ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbm_assessment_topic ADD COLUMN "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

