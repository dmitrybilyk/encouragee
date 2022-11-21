CREATE SEQUENCE callrec.seq_users;

GRANT UPDATE ON TABLE callrec.seq_users TO callrecgrp;

CREATE SEQUENCE callrec.seq_useraudit
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE callrec.seq_useraudit OWNER TO callrecgrp;
