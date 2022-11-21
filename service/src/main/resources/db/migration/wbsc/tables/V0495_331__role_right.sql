-- Table: wbsc.role_right

-- DROP TABLE wbsc.role_right;

CREATE TABLE wbsc.role_right
(
  rightid integer NOT NULL,
  roleid integer NOT NULL,
  CONSTRAINT role_right_pkey PRIMARY KEY (rightid, roleid),
  CONSTRAINT right_fk FOREIGN KEY (rightid)
      REFERENCES wbsc.rights (rightid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT role_fk FOREIGN KEY (roleid)
      REFERENCES wbsc.roles (roleid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.role_right OWNER TO postgres;
COMMENT ON TABLE wbsc.role_right IS 'M:N table';

GRANT select, update, insert, delete on wbsc.role_right to wbscgrp;
