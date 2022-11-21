-- CREATE TABLE couple_extdata {{{
-- external data for couples
CREATE TABLE callrec.couple_extdata (
  cplid INTEGER NOT NULL,
  key           VARCHAR(255) NOT NULL,
  value         TEXT NOT NULL DEFAULT '',
  created_ts    TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT _couple_extdata_pk PRIMARY KEY (cplid, key, value),
  CONSTRAINT _couple_extdata_fk
    FOREIGN KEY (cplid) REFERENCES callrec.couples (id) ON DELETE CASCADE
) WITHOUT OIDS;

ALTER TABLE callrec.couple_extdata OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.couple_extdata TO callrecgrp;

COMMENT ON TABLE callrec.couple_extdata IS 'Additional data to each call are stored here.';
-- }}}

