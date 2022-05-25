-- ALTER TABLE "tbl_assessment_topic" DROP "recommendation";


create table tbl_assessment_topic_recommendation (
                                      recommendation_id SERIAL PRIMARY KEY,
                                      assessment int references tbl_assessment(assessment_id),
                                      topic  int references tbm_assessment_topic(topic_id),
                                      recommendation text,
                                      impact varchar(50) CHECK(impact='HIGH' OR impact='MEDIUM' OR impact='LOW'),
                                      effort varchar(50) CHECK(effort='HIGH' OR effort='MEDIUM' OR effort='LOW'),
                                      delivery_horizon text,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);