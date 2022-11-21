-- Table: wbsc.questiongroups

-- DROP TABLE wbsc.questiongroups;

CREATE TABLE wbsc.questiongroups
(
  questiongroupid integer NOT NULL DEFAULT nextval('wbsc.seq_questiongroups'::regclass),
  groupname character varying(100) NOT NULL,
  qformid integer NOT NULL,
  group_value real, -- wage (in percentage)
  group_order integer NOT NULL,
  description text,
  CONSTRAINT groups_pkey PRIMARY KEY (questiongroupid),
  CONSTRAINT questforms_fk FOREIGN KEY (qformid)
      REFERENCES wbsc.questforms (qformid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.questiongroups OWNER TO postgres;
COMMENT ON TABLE wbsc.questiongroups IS 'groups of questions';
COMMENT ON COLUMN wbsc.questiongroups.group_value IS 'wage (in percentage)';

GRANT select, update, insert, delete on wbsc.questiongroups to wbscgrp;

