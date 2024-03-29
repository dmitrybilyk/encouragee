
ALTER TABLE WBSC.EVALANSWERS ADD COLUMN TOTAL_ANSWER_VALUE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.EVALANSWERS SET TOTAL_ANSWER_VALUE_NUMERIC = TOTAL_ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS DROP COLUMN TOTAL_ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS  RENAME COLUMN TOTAL_ANSWER_VALUE_NUMERIC TO TOTAL_ANSWER_VALUE;




ALTER TABLE WBSC.EVALANSWERS ADD COLUMN GROUP_ANSWER_VALUE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.EVALANSWERS SET GROUP_ANSWER_VALUE_NUMERIC = GROUP_ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS DROP COLUMN GROUP_ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS  RENAME COLUMN GROUP_ANSWER_VALUE_NUMERIC TO GROUP_ANSWER_VALUE;




ALTER TABLE WBSC.EVALANSWERS ADD COLUMN QUESTION_ANSWER_VALUE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.EVALANSWERS SET QUESTION_ANSWER_VALUE_NUMERIC = QUESTION_ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS DROP COLUMN QUESTION_ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS  RENAME COLUMN QUESTION_ANSWER_VALUE_NUMERIC TO QUESTION_ANSWER_VALUE;




ALTER TABLE WBSC.EVALANSWERS ADD COLUMN ANSWER_VALUE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.EVALANSWERS SET ANSWER_VALUE_NUMERIC = ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS DROP COLUMN ANSWER_VALUE;

ALTER TABLE WBSC.EVALANSWERS RENAME COLUMN ANSWER_VALUE_NUMERIC TO ANSWER_VALUE;




ALTER TABLE WBSC.EVALUATIONS ADD COLUMN SCORE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.EVALUATIONS SET SCORE_NUMERIC = SCORE;

ALTER TABLE WBSC.EVALUATIONS DROP COLUMN SCORE;

ALTER TABLE WBSC.EVALUATIONS  RENAME COLUMN SCORE_NUMERIC TO SCORE;




ALTER TABLE WBSC.SUBEVALUATION ADD COLUMN SUB_EVAL_TOTAL_NUMERIC NUMERIC(12,6);

UPDATE WBSC.SUBEVALUATION SET SUB_EVAL_TOTAL_NUMERIC = SUB_EVAL_TOTAL;

ALTER TABLE WBSC.SUBEVALUATION DROP COLUMN SUB_EVAL_TOTAL;

ALTER TABLE WBSC.SUBEVALUATION  RENAME COLUMN SUB_EVAL_TOTAL_NUMERIC TO SUB_EVAL_TOTAL;




ALTER TABLE WBSC.ANSWERS ADD COLUMN ANSWER_VALUE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.ANSWERS SET ANSWER_VALUE_NUMERIC = ANSWER_VALUE;

ALTER TABLE WBSC.ANSWERS DROP COLUMN ANSWER_VALUE;

ALTER TABLE WBSC.ANSWERS  RENAME COLUMN ANSWER_VALUE_NUMERIC TO ANSWER_VALUE;




ALTER TABLE WBSC.QUESTFORMS ADD COLUMN REPORT_WEIGHT_NUMERIC NUMERIC(12,6);

UPDATE WBSC.QUESTFORMS SET REPORT_WEIGHT_NUMERIC = REPORT_WEIGHT;

ALTER TABLE WBSC.QUESTFORMS DROP COLUMN REPORT_WEIGHT;

ALTER TABLE WBSC.QUESTFORMS  RENAME COLUMN REPORT_WEIGHT_NUMERIC TO REPORT_WEIGHT;




ALTER TABLE WBSC.QUESTIONS ADD COLUMN QUESTION_VALUE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.QUESTIONS SET QUESTION_VALUE_NUMERIC = QUESTION_VALUE;

ALTER TABLE WBSC.QUESTIONS DROP COLUMN QUESTION_VALUE;

ALTER TABLE WBSC.QUESTIONS  RENAME COLUMN QUESTION_VALUE_NUMERIC TO QUESTION_VALUE;




ALTER TABLE WBSC.QUESTIONGROUPS ADD COLUMN GROUP_VALUE_NUMERIC NUMERIC(12,6);

UPDATE WBSC.QUESTIONGROUPS SET GROUP_VALUE_NUMERIC = GROUP_VALUE;

ALTER TABLE WBSC.QUESTIONGROUPS DROP COLUMN GROUP_VALUE;

ALTER TABLE WBSC.QUESTIONGROUPS  RENAME COLUMN GROUP_VALUE_NUMERIC TO GROUP_VALUE;







