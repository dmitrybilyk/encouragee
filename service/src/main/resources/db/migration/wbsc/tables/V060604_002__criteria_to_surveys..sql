CREATE TABLE wbsc.criteria_to_surveys
(
  criteria_to_surveys_id INTEGER NOT NULL DEFAULT nextval(('wbsc.seq_criteria_to_surveys'::text)::regclass),
  criteria_id            INTEGER NOT NULL,
  min_score              INTEGER,
  max_score              INTEGER,
  any_system             BOOLEAN,
  points_only            BOOLEAN,
  percentage_only        BOOLEAN,
  CONSTRAINT criteriaToSurveysId PRIMARY KEY (criteria_to_surveys_id),
  CONSTRAINT FKcriteria FOREIGN KEY (criteria_id)
    REFERENCES wbsc.criteria (criteriaid) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE CASCADE
);

ALTER TABLE wbsc.criteria_to_surveys
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.criteria_to_surveys TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.criteria_to_surveys TO wbscgrp;
