ALTER TABLE wbsc.questforms ALTER COLUMN qftype TYPE evaluation_type;

ALTER TABLE wbsc.questforms DROP CONSTRAINT qform_qftype_ck;
