-- Table: wbsc.externaldata

-- DROP TABLE wbsc.externaldata;

CREATE TABLE wbsc.externaldata
(
  externaldataid integer NOT NULL DEFAULT nextval('wbsc.seq_externaldata'::regclass),
  criteriaid integer,
  ordering integer NOT NULL, -- order of records in a subcriteria
  "key" character varying(255) NOT NULL, -- key in external
  comparator wbsc.externaldata_comparator_domain NOT NULL, -- such as <, >, >=
  format wbsc.externaldata_format_domain NOT NULL, -- number, text
  "value" text,
  "operator" wbsc.externaldata_operator_domain, -- and, or
  CONSTRAINT externaldata_pkey PRIMARY KEY (externaldataid),
  CONSTRAINT subcriteria_fk FOREIGN KEY (criteriaid)
      REFERENCES wbsc.criteria (criteriaid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.externaldata OWNER TO postgres;
COMMENT ON TABLE wbsc.externaldata IS 'defines a more specified selection based on external data';
COMMENT ON COLUMN wbsc.externaldata.ordering IS 'order of records in a subcriteria';
COMMENT ON COLUMN wbsc.externaldata."key" IS 'key in external';
COMMENT ON COLUMN wbsc.externaldata.comparator IS 'such as <, >, >=';
COMMENT ON COLUMN wbsc.externaldata.format IS 'number, text';
COMMENT ON COLUMN wbsc.externaldata."operator" IS 'and, or';

GRANT select, update, insert, delete on wbsc.externaldata to wbscgrp;

