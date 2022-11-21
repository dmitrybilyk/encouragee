-- CREATE TABLE decoding_requests {{{
-- see the decode_queue view
CREATE TABLE callrec.decoding_requests (
  id INTEGER NOT NULL,
  created TIMESTAMP WITH TIME ZONE not null DEFAULT now(),
  CONSTRAINT decoding_requests_pk PRIMARY KEY (id)
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.decoding_requests TO GROUP callrecgrp;

COMMENT ON TABLE callrec.decoding_requests IS 'decoding requests indicator';
COMMENT ON COLUMN callrec.decoding_requests.id IS 'decoding request id';
COMMENT ON COLUMN callrec.decoding_requests.created IS 'decoding request insertion';
-- }}}

