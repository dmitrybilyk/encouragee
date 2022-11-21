CREATE TABLE callrec.view_restrictions
(
  id integer NOT NULL,
  "name" character varying(50),
  is_active boolean DEFAULT true,
  CONSTRAINT view_restriction_pk PRIMARY KEY (id)
)
WITHOUT OIDS;
ALTER TABLE callrec.view_restrictions OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.view_restrictions TO callrecgrp;
COMMENT ON TABLE callrec.view_restrictions IS 'This table is used for grouping view_parts. Name and is active columns are not used yet.';
