-- Table: callrec.managedkeys

-- DROP TABLE callrec.managed_keys;

CREATE TABLE callrec.managed_keys
(
  uuid character varying NOT NULL,
  purpose character varying,
  algorithm character varying,
  strength integer,
  activationdate date,
  expirydate date,
  compromised boolean,
  irrecoverable boolean,
  archived boolean,
  CONSTRAINT _managedkey_pkey PRIMARY KEY (uuid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE callrec.managed_keys OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.managed_keys TO callrecgrp;
