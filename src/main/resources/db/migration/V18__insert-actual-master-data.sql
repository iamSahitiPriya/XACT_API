


INSERT INTO tbl_assessment_topic_recommendation (assessment, topic,recommendation,created_at,updated_at) SELECT
    assessment_id,topic_id,recommendation,created_at,updated_at FROM tbl_assessment_topic;


INSERT INTO tbl_assessment_parameter_recommendation (assessment,parameter,recommendation,created_at,updated_at) SELECT
      assessment_id,parameter_id,recommendation,created_at,updated_at FROM tbl_assessment_parameter;


ALTER TABLE "tbl_assessment_topic" DROP "recommendation";

ALTER TABLE "tbl_assessment_parameter" DROP "recommendation";