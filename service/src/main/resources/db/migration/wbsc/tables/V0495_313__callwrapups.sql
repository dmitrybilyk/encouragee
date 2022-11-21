-- Table: wbsc.callwrapups

-- DROP TABLE wbsc.callwrapups;

CREATE TABLE wbsc.callwrapups
(
  callwrapupid integer NOT NULL DEFAULT nextval('wbsc.seq_callwrapups'::regclass),
  "key" character varying(100),
  description character varying(100),
  company integer NOT NULL,
  CONSTRAINT callwrapups_pkey PRIMARY KEY (callwrapupid),
  CONSTRAINT callwrapups_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT callwrapups_key_key UNIQUE (key)
)
WITHOUT OIDS;
ALTER TABLE wbsc.callwrapups OWNER TO postgres;
COMMENT ON TABLE wbsc.callwrapups IS 'translation of external data to an eligible form';

GRANT select, update, insert, delete on wbsc.callwrapups to wbscgrp;
