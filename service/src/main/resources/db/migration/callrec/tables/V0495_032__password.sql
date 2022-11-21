-- Table: passwords

-- DROP TABLE passwords;

CREATE TABLE callrec.passwords
(
  userid integer NOT NULL,
  "password" character varying(255),
  salt character varying(255),
  creation_date timestamp with time zone NOT NULL,
  CONSTRAINT login_userid_fkey FOREIGN KEY (userid)
      REFERENCES callrec.users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE callrec.passwords OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.passwords TO callrecgrp;
