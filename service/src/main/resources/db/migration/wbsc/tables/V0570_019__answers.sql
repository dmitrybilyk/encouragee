ALTER TABLE WBSC.ANSWERS ADD COLUMN CREATE_ORDER INTEGER;
UPDATE WBSC.ANSWERS SET CREATE_ORDER=ANSWER_ORDER;
ALTER TABLE WBSC.ANSWERS ALTER COLUMN CREATE_ORDER SET NOT NULL;
