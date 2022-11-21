  CREATE TABLE wbsc.criteria_to_speech_tags
  (
    criteriaToSpeechTagid   INTEGER NOT NULL DEFAULT nextval(('wbsc.seq_criteria_to_speech_tags'::text)::regclass),
    criteriaId              INTEGER NOT NULL,
    speechTagId             INTEGER NOT NULL,
    confidenceFrom          INTEGER NOT NULL,
    excluded                BOOLEAN NOT NULL,
    CONSTRAINT criteriaToSpeechTagid PRIMARY KEY (criteriaToSpeechTagid),
    CONSTRAINT FKcriteria FOREIGN KEY (criteriaId)
    REFERENCES wbsc.criteria (criteriaid) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE CASCADE
  );

  ALTER TABLE wbsc.criteria_to_speech_tags OWNER TO postgres;
  GRANT ALL ON TABLE wbsc.criteria_to_speech_tags  TO postgres;
  GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.criteria_to_speech_tags  TO wbscgrp;
