-- skipped number V520_015 to be synchronized with oracle
DROP INDEX IF EXISTS _couples_evaluated_idx;

CREATE INDEX _couples_scorecard_use_idx ON callrec.couples (scorecard_use);
