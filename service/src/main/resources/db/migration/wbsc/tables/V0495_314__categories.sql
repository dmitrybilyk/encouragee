-- Table: wbsc.categories

-- DROP TABLE wbsc.categories;

CREATE TABLE wbsc.categories
(
  categoryid integer NOT NULL DEFAULT nextval('wbsc.seq_categories'::regclass),
  category_name character varying(255),
  company integer NOT NULL,
  CONSTRAINT categories_pkey PRIMARY KEY (categoryid),
  CONSTRAINT categories_company_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT categories_category_name_key UNIQUE (category_name)
)
WITHOUT OIDS;
ALTER TABLE wbsc.categories OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.categories to wbscgrp;
