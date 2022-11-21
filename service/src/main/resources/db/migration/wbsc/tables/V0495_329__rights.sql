-- Table: wbsc.rights

-- DROP TABLE wbsc.rights;

CREATE TABLE wbsc.rights
(
  rightid integer NOT NULL DEFAULT nextval('wbsc.seq_rights'::regclass),
  "name" character varying(50),
  CONSTRAINT rights_pkey PRIMARY KEY (rightid),
  CONSTRAINT right_unique UNIQUE (name)
)
WITHOUT OIDS;
ALTER TABLE wbsc.rights OWNER TO postgres;
COMMENT ON TABLE wbsc.rights IS 'rights defined in the application. No user defined rights are allowed.';

GRANT select, update, insert, delete on wbsc.rights to wbscgrp;
