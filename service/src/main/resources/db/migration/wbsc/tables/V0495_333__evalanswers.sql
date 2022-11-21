-- Table: wbsc.evalanswers

-- DROP TABLE wbsc.evalanswers;

CREATE TABLE wbsc.evalanswers
(
  evalanswerid integer NOT NULL DEFAULT nextval('wbsc.seq_evalanswers'::regclass),
  subevaluationid integer,
  answerid integer,
  total_answer_value real, -- wage of this answer in the whole form. (group_answer_value, 0 or MAX)
  group_answer_value real, -- wage of this answer in a group (answer * question * group, 0 or MAX)
  question_answer_value real, -- wage of this answer in a question (answer * question)
  answer_value real, -- value of the chosen answer
  note text,
  CONSTRAINT evalanswers_pkey PRIMARY KEY (evalanswerid),
  CONSTRAINT answers_fk FOREIGN KEY (answerid)
      REFERENCES wbsc.answers (answerid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT subevaluation_fk FOREIGN KEY (subevaluationid)
      REFERENCES wbsc.subevaluation (subevaluationid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.evalanswers OWNER TO postgres;
COMMENT ON COLUMN wbsc.evalanswers.total_answer_value IS 'wage of this answer in the whole form. (group_answer_value, 0 or MAX)';
COMMENT ON COLUMN wbsc.evalanswers.group_answer_value IS 'wage of this answer in a group (answer * question * group, 0 or MAX)';
COMMENT ON COLUMN wbsc.evalanswers.question_answer_value IS 'wage of this answer in a question (answer * question)';
COMMENT ON COLUMN wbsc.evalanswers.answer_value IS 'value of the chosen answer';

GRANT select, update, insert, delete on wbsc.evalanswers to wbscgrp;

