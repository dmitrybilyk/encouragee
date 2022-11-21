CREATE TABLE wbsc.passwords
(
  passwordid integer not null default nextval('wbsc.seq_password'::regclass),
  userid integer NOT NULL,
  "password" character varying(255),
  salt character varying(255),
  creation_date timestamp with time zone NOT NULL,
  CONSTRAINT login_sc_userid_fkey FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (OIDS=FALSE);
ALTER TABLE wbsc.passwords OWNER TO postgres;
GRANT ALL ON TABLE wbsc.passwords TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.passwords TO wbscgrp;
