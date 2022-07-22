-- ALTER TABLE "tbl_assessment_topic" DROP "recommendation";


create table tbl_assessment_topic_recommendation (
                                      assessment int references tbl_assessment(assessment_id),
                                      topic  int references tbm_assessment_topic(topic_id),
                                      recommendation_id INT PRIMARY KEY,
                                      recommendation text,
                                      impact varchar(20) CHECK(impact='HIGH' OR impact='MEDIUM' OR impact='LOW'),
                                      effect varchar(20) CHECK(effect='HIGH' OR effect='MEDIUM' OR effect='LOW'),
                                      delivery_horizon text,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);