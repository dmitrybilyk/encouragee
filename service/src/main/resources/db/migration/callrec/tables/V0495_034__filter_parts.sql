-- CREATE TABLE filter_parts {{{
CREATE TABLE callrec.filter_parts (
  filterid INTEGER NOT NULL,
  formfield VARCHAR(255) NOT NULL,
  colname VARCHAR(255) NOT NULL,
  sqlval VARCHAR(255) NOT NULL,
  userval VARCHAR(255) NOT NULL,
  isnumeric BOOLEAN NOT NULL,
  predicate VARCHAR(20) NOT NULL,
  operator VARCHAR(20) NOT NULL,
  extdata_key VARCHAR(255),
  created_ts TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT _filter_parts_pk PRIMARY KEY (filterid, formfield, colname),
  CONSTRAINT _filter_parts_filterid_fk
    FOREIGN KEY (filterid) REFERENCES callrec.filters (id) ON DELETE CASCADE
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.filter_parts TO GROUP callrecgrp;

COMMENT ON TABLE callrec.filter_parts IS 'Belongs to filters and holds details about each field in search form. Sql question can be reconstructed.';
-- }}}

