-- CREATE TABLE role_actions {{{
CREATE TABLE callrec.role_actions (
    roleid INTEGER NOT NULL,
    actionid INTEGER NOT NULL,
    CONSTRAINT _role_actions_roleid_fk
      FOREIGN KEY (roleid) REFERENCES callrec.roles (id) ON DELETE CASCADE,
    CONSTRAINT _role_actions_actionid_fk
      FOREIGN KEY (actionid) REFERENCES callrec.actions (id),
    CONSTRAINT _role_actions_pk PRIMARY KEY (roleid, actionid)

) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.role_actions TO GROUP callrecgrp;
-- }}}

