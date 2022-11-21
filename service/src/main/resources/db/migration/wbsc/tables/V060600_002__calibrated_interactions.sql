CREATE TABLE wbsc.calibrated_interactions
(
  calibrated_interaction_id integer DEFAULT "nextval"('"seq_calibrated_interactions"'::"regclass") NOT NULL,
  conversation_id  VARCHAR,
  CONSTRAINT calibrated_interaction_id_pkey PRIMARY KEY (calibrated_interaction_id)
)
  WITH (
    OIDS=FALSE
  );
ALTER TABLE wbsc.calibrated_interactions
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.calibrated_interactions TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.calibrated_interactions TO wbscgrp;
