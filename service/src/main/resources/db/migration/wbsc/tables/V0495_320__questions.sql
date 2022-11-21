-- Table: wbsc.questions

-- DROP TABLE wbsc.questions;

CREATE TABLE wbsc.questions
(
  questionid integer NOT NULL DEFAULT nextval('wbsc.seq_questions'::regclass),
  question_text text NOT NULL,
  groupid integer NOT NULL,
  question_value real, -- wage (in percentage)
  question_order integer NOT NULL,
  description text,
  CONSTRAINT questions_pkey PRIMARY KEY (questionid),
  CONSTRAINT questions_fk FOREIGN KEY (groupid)
      REFERENCES wbsc.questiongroups (questiongroupid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.questions OWNER TO postgres;
COMMENT ON COLUMN wbsc.questions.question_value IS 'wage (in percentage)';

GRANT select, update, insert, delete on wbsc.questions to wbscgrp;

