-- Table: wbsc.ccgroups

-- DROP TABLE wbsc.ccgroups;

CREATE TABLE wbsc.ccgroups
(
  ccgroupid integer NOT NULL DEFAULT nextval('wbsc.seq_ccgroups'::regclass),
  ccgroupname character varying(50),
  description text,
  parentid integer,
  leftindex integer NOT NULL,
  rightindex integer NOT NULL,
  company integer NOT NULL,
  external_id character varying(255),
  CONSTRAINT ccgroups_pkey PRIMARY KEY (ccgroupid),
  CONSTRAINT ccgroups_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ccgroups_parent_fk FOREIGN KEY (parentid)
      REFERENCES wbsc.ccgroups (ccgroupid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ccgroups_ccgroupname_key UNIQUE (ccgroupname,company)
)
WITHOUT OIDS;
ALTER TABLE wbsc.ccgroups OWNER TO postgres;
COMMENT ON TABLE wbsc.ccgroups IS 'definition of groups';

GRANT select, update, insert, delete on wbsc.ccgroups to wbscgrp;
