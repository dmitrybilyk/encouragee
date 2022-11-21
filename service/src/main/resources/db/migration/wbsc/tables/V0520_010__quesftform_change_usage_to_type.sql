ALTER TABLE wbsc.questforms
  ADD qftype character varying(50) NOT NULL DEFAULT 'QUALITY';

UPDATE wbsc.questforms
  SET qftype=usage;

ALTER TABLE wbsc.questforms
  ADD CONSTRAINT qform_qftype_ck CHECK (qftype IN ('QUALITY', 'SURVEY', 'TRAINING'));

ALTER TABLE wbsc.questforms
  DROP CONSTRAINT qform_usage_ck;

ALTER TABLE wbsc.questforms
  DROP COLUMN usage;
