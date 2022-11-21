ALTER TABLE wbsc.evaluations
  ALTER COLUMN apply_to_statistics SET DEFAULT true;

ALTER TABLE wbsc.evaluations
  ALTER COLUMN apply_to_statistics SET NOT NULL;