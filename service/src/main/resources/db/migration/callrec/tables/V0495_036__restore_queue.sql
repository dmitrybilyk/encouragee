-- $Id: restore_queue.sql,v 1.9 2008-08-12 07:38:42 mensik Exp $
-- CREATE TABLE restore_queue {{{
CREATE TABLE callrec.restore_queue (
	id INTEGER NOT NULL DEFAULT nextval('callrec.seq_restore_queue'),
  -- call id
  callid INTEGER NOT NULL,
  -- couple id
  cplid INTEGER NOT NULL,
  flag VARCHAR(20),
  exp TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL,
  archive VARCHAR(255) NOT NULL,
  updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT _restore_queue_pk PRIMARY KEY (id),
  CONSTRAINT _restore_queue_cplid_fk
    FOREIGN KEY (cplid) REFERENCES callrec.couples (id) ON DELETE CASCADE,
  CONSTRAINT _restore_queue_callid_fk
    FOREIGN KEY (callid) REFERENCES callrec.calls (id) ON DELETE CASCADE,
  CONSTRAINT _restore_queue_unique_call_cpl UNIQUE(callid, cplid)
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.restore_queue TO GROUP callrecgrp;

COMMENT ON TABLE callrec.restore_queue IS 'Archived and deleted couples can be resotred. Here is info about couples that will be restored.';
-- }}}

