ALTER TABLE wbsc.questions DROP CONSTRAINT questions_question_type_ck;

ALTER TABLE wbsc.questions
  ADD CONSTRAINT questions_question_type_ck CHECK (question_type IN ('REGULAR_QUESTION', 'CUSTOMER_FEEDBACK', 'PROMPT_ONLY', 'NPS_QUESTION'));
