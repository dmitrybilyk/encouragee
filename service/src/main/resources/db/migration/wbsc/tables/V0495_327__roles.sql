-- Table: wbsc.roles

-- DROP TABLE wbsc.roles;

CREATE TABLE wbsc.roles
(
  roleid integer NOT NULL DEFAULT nextval('wbsc.seq_roles'::regclass),
  "name" character varying(255),
  description text,
  company integer NOT NULL,
  CONSTRAINT roles_pkey PRIMARY KEY (roleid),
  CONSTRAINT roles_comapny_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITHOUT OIDS;
ALTER TABLE wbsc.roles OWNER TO postgres;
COMMENT ON TABLE wbsc.roles IS 'holds roles (sets of rules).';

GRANT select, update, insert, delete on wbsc.roles to wbscgrp;
