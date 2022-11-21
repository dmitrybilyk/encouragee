-- Table: wbsc.appoptions

-- DROP TABLE wbsc.appoptions;

CREATE TABLE wbsc.appoptions
(
  appoptionid integer NOT NULL DEFAULT nextval('wbsc.seq_appoptions'::regclass),
  "key" character varying(50) NOT NULL,
  "value" text NOT NULL,
  "comment" text,
  company integer NOT NULL,
  CONSTRAINT appoptions_pkey PRIMARY KEY (appoptionid),
  CONSTRAINT appoptions_company_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT appoptions_unique UNIQUE (key, company)
)
WITHOUT OIDS;
ALTER TABLE wbsc.appoptions OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.appoptions to wbscgrp;
