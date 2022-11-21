-- Table: wbsc.companies

-- DROP TABLE wbsc.companies;

CREATE TABLE wbsc.companies
(
  companyid integer NOT NULL DEFAULT nextval('wbsc.seq_companies'::regclass),
  display_name character varying(50),
  configuration_equal_group character varying(255),
  description text,
  CONSTRAINT companies_pkey PRIMARY KEY (companyid)
)
WITHOUT OIDS;
ALTER TABLE wbsc.companies OWNER TO postgres;

alter table wbsc.companies
add constraint companies_unique_name unique(display_name);

GRANT select, update, insert, delete on wbsc.companies to wbscgrp;
