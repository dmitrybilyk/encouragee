-- CREATE TABLE filters {{{
CREATE TABLE callrec.filters (
  id INTEGER NOT NULL,
  ownerid INTEGER NOT NULL,
  name VARCHAR(255) NOT NULL,
  ispublic BOOLEAN NOT NULL,
  israndom BOOLEAN NOT NULL,
  pagelen INTEGER NOT NULL,
  created_ts TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT _filters_pk PRIMARY KEY (id),
  CONSTRAINT _filters_ownerid_fk
    FOREIGN KEY (ownerid) REFERENCES callrec.users (id) ON DELETE CASCADE
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.filters TO GROUP callrecgrp;

COMMENT ON TABLE callrec.filters IS 'Saved search filters. Details of filters is saved in filter_parts.';
-- }}}

