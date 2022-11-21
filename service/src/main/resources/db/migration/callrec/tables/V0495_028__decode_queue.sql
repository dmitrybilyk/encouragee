-- CREATE TABLE decode_queue {{{
-- see the decode_queue view
CREATE TABLE callrec.decode_queue (
  -- decoder-provided queue id
  dqid VARCHAR(25) NOT NULL,
  -- streamgroup id
  sgid INTEGER NOT NULL,
  CONSTRAINT _decode_queue_pk PRIMARY KEY (dqid, sgid)
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.decode_queue TO GROUP callrecgrp;

COMMENT ON TABLE callrec.decode_queue IS 'Cfiles that are not decoded - sgid.';
COMMENT ON COLUMN callrec.decode_queue.dqid IS 'Decoder-provided queue id';
COMMENT ON COLUMN callrec.decode_queue.sgid IS 'streamgroup id';
-- }}}

