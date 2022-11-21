ALTER TABLE wbsc.questions
  ADD question_type character varying(50) NOT NULL DEFAULT 'CLOSED_ENDED';

ALTER TABLE wbsc.questions
  ADD CONSTRAINT questions_question_type_ck CHECK (question_type IN ('CLOSED_ENDED', 'OPEN_ENDED', 'PROMPT_ONLY'));

UPDATE wbsc.questions
  SET question_type='OPEN_ENDED' WHERE open_ended=true;

ALTER TABLE wbsc.questions
  DROP COLUMN open_ended;
