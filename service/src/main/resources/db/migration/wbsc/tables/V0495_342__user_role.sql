-- Table: wbsc.user_role

-- DROP TABLE wbsc.user_role;

CREATE TABLE wbsc.user_role
(
  userid integer NOT NULL,
  roleid integer NOT NULL,
  CONSTRAINT user_role_pkey PRIMARY KEY (userid, roleid),
  CONSTRAINT role_fk FOREIGN KEY (roleid)
      REFERENCES wbsc.roles (roleid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT user_fk FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.user_role OWNER TO postgres;
COMMENT ON TABLE wbsc.user_role IS 'M:N table';

GRANT select, update, insert, delete on wbsc.user_role to wbscgrp;
