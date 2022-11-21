-- $Id: users_restore_queue.sql,v 1.5 2008-08-12 07:38:42 mensik Exp $
CREATE TABLE callrec.users_restore_queue (
  userid INTEGER NOT NULL,
  restore_queue_id INTEGER NOT NULL,
  exp TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT _users_restore_queue_pk
    PRIMARY KEY (userid, restore_queue_id),
  CONSTRAINT _users_restore_queue_fk_users
    FOREIGN KEY (userid) REFERENCES callrec.users (id) ON DELETE CASCADE,
  CONSTRAINT _users_restore_queue_fk_restore_queue
    FOREIGN KEY (restore_queue_id) REFERENCES callrec.restore_queue (id) ON DELETE CASCADE
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.users_restore_queue TO GROUP callrecgrp;

COMMENT ON TABLE callrec.users_restore_queue IS 'Couples that users want to restore. Exp is expiration time- time when archive will be deleted again.';
