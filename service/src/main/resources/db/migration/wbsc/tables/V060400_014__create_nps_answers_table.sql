CREATE TABLE wbsc.nps_answers
(
  evalanswerid          INTEGER NOT NULL DEFAULT nextval('wbsc.seq_nps_answers'::regclass),
  subevaluationid       INTEGER,
  evaluationid          INTEGER,
  qformid               INTEGER,
  answerid              INTEGER,
  answer_key            VARCHAR(50),
  evaluation_date   	  TIMESTAMP,
  evaluated_user        INTEGER,
  CONSTRAINT nps_answers_pkey PRIMARY KEY (evalanswerid),
  CONSTRAINT answers_fk FOREIGN KEY (answerid)
  REFERENCES wbsc.answers (answerid) MATCH SIMPLE
  ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT subevaluation_fk FOREIGN KEY (subevaluationid)
  REFERENCES wbsc.subevaluation (subevaluationid) MATCH SIMPLE
  ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT evaluation_fk FOREIGN KEY (evaluationid)
  REFERENCES wbsc.evaluations (evaluationid) MATCH SIMPLE
  ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.nps_answers OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.nps_answers to wbscgrp;

