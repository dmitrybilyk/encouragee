-- CREATE TABLE actions {{{
CREATE TABLE callrec.actions (
  id INTEGER NOT NULL,
  name VARCHAR(100) DEFAULT '',
  description VARCHAR(255) DEFAULT '',
  created_ts TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT _actions_pk PRIMARY KEY (id)
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.actions TO GROUP callrecgrp;

COMMENT ON TABLE callrec.actions IS 'All actions that can be performed by users.
 They are connected via role_actions and are in negative.
 This means that if role has sam action then this action is forbidden for current role.';
-- }}}

