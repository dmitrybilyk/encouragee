-- Table: wbsc.user_right

-- DROP TABLE wbsc.user_right;

CREATE TABLE wbsc.user_right
(
  userid integer NOT NULL,
  rightid integer NOT NULL,
  CONSTRAINT user_right_pkey PRIMARY KEY (userid, rightid),
  CONSTRAINT right_fk FOREIGN KEY (rightid)
      REFERENCES wbsc.rights (rightid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT user_fk FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.user_right OWNER TO postgres;
COMMENT ON TABLE wbsc.user_right IS 'M:N table';

GRANT select, update, insert, delete on wbsc.user_right to wbscgrp;
