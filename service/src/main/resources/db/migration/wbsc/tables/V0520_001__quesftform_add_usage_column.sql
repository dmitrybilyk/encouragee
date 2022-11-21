ALTER TABLE wbsc.questforms
  ADD usage character varying(50) NOT NULL DEFAULT 'QUALITY';

ALTER TABLE wbsc.questforms
  ADD CONSTRAINT qform_usage_ck CHECK (usage IN ('QUALITY', 'SURVEY', 'TRAINING'));
