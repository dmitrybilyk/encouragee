-- CREATE TABLE call_extdata {{{
-- external data for calls
CREATE TABLE callrec.call_extdata (
  callid INTEGER NOT NULL,
  key           VARCHAR(255) NOT NULL,
  value         TEXT NOT NULL DEFAULT '',
  created_ts TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT _call_extdata_pk PRIMARY KEY (callid, key, value),
  CONSTRAINT _call_extdata_fk
    FOREIGN KEY (callid) REFERENCES callrec.calls (id) ON DELETE CASCADE
) WITHOUT OIDS;


GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.call_extdata TO GROUP callrecgrp;

COMMENT ON TABLE callrec.call_extdata IS 'Additional data that are added to call.';
-- }}}

