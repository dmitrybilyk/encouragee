-- Table: wbsc."database"

-- DROP TABLE wbsc."database";

CREATE TABLE wbsc."database"
(
  databaseid integer NOT NULL DEFAULT nextval('wbsc.seq_database'::regclass),
  "name" character varying(50) NOT NULL, -- presentation name
  configuration_equal_group character varying(255) NOT NULL,
  dbtype wbsc.database_type_domain NOT NULL DEFAULT 'LOCAL'::character varying, -- defines the way of how to handle a connection
  encryption_type wbsc.database_encryption_type_domain NOT NULL,
  "connection" character varying(255), -- connection string to a database
  connection_login character varying(255),
  connection_password character varying(100),
  company integer NOT NULL,
  CONSTRAINT database_pkey PRIMARY KEY (databaseid),
  CONSTRAINT database_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc."database" OWNER TO postgres;
COMMENT ON TABLE wbsc."database" IS 'contains logical names of database pools in a configuration service';
COMMENT ON COLUMN wbsc."database"."name" IS 'presentation name';
COMMENT ON COLUMN wbsc."database".dbtype IS 'defines the way of how to handle a connection';
COMMENT ON COLUMN wbsc."database"."connection" IS 'connection string to a database';

GRANT select, update, insert, delete on wbsc.database to wbscgrp;

