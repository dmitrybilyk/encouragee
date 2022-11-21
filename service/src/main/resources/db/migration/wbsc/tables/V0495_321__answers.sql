-- Table: wbsc.answers

-- DROP TABLE wbsc.answers;

CREATE TABLE wbsc.answers
(
  answerid integer NOT NULL DEFAULT nextval('wbsc.seq_answers'::regclass),
  answer_text text NOT NULL,
  answer_value real, -- wage or points
  questionid integer NOT NULL,
  answer_order integer NOT NULL,
  description text,
  compliance wbsc.answers_compliance_domain NOT NULL,
  CONSTRAINT answers_pkey PRIMARY KEY (answerid),
  CONSTRAINT questions_fk FOREIGN KEY (questionid)
      REFERENCES wbsc.questions (questionid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.answers OWNER TO postgres;
COMMENT ON COLUMN wbsc.answers.answer_value IS 'wage or points';

GRANT select, update, insert, delete on wbsc.answers to wbscgrp;
