CREATE TABLE wbsc.calibrated_inter_evaluation
(
  evaluation_id INTEGER NOT NULL,
  calibrated_interaction_id INTEGER,

  CONSTRAINT calibrated_interaction_id_fk FOREIGN KEY (calibrated_interaction_id)
    REFERENCES wbsc.calibrated_interactions (calibrated_interaction_id) MATCH SIMPLE,
  CONSTRAINT calibrated_inter_evaluation_fk FOREIGN KEY (evaluation_id)
    REFERENCES wbsc.evaluations (evaluationid) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
)
  WITH (
    OIDS=FALSE
  );

ALTER TABLE wbsc.calibrated_inter_evaluation
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.calibrated_inter_evaluation TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.calibrated_inter_evaluation TO wbscgrp;
