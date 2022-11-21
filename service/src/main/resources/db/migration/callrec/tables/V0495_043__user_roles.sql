-- CREATE TABLE user_roles {{{
CREATE TABLE callrec.user_roles (
  userid INTEGER NOT NULL,
  roleid INTEGER NOT NULL,
  CONSTRAINT _user_roles_pk
    PRIMARY KEY (userid, roleid),
  CONSTRAINT _user_roles_userid_fk
    FOREIGN KEY (userid) REFERENCES callrec.users(id) ON DELETE CASCADE,
  CONSTRAINT _user_roles_roleid_fk
    FOREIGN KEY (roleid) REFERENCES callrec.roles(id)
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.user_roles TO GROUP callrecgrp;
-- }}}

