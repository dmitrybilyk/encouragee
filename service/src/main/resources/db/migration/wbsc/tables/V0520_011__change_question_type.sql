ALTER TABLE wbsc.questions
  DROP CONSTRAINT questions_question_type_ck;

ALTER TABLE wbsc.questions
  ALTER COLUMN question_type SET DEFAULT 'REGULAR_QUESTION';

UPDATE wbsc.questions
  SET question_type='REGULAR_QUESTION' WHERE question_type!='OPEN_ENDED';

UPDATE wbsc.questions
  SET question_type='CUSTOMER_FEEDBACK' WHERE question_type='OPEN_ENDED';

ALTER TABLE wbsc.questions
  ADD CONSTRAINT questions_question_type_ck CHECK (question_type IN ('REGULAR_QUESTION', 'CUSTOMER_FEEDBACK', 'PROMPT_ONLY'));
