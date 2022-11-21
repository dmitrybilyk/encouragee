-- SC-6006
ALTER DOMAIN callrec.evaluate_mark
DROP CONSTRAINT dom_evaluate_mark;

ALTER DOMAIN callrec.evaluate_mark
ADD CONSTRAINT dom_scorecard_usage CHECK (value ~ '^[EBTS]*$');

ALTER TABLE callrec.couples RENAME COLUMN evaluated TO scorecard_use;
