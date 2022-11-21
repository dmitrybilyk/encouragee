CREATE SEQUENCE callrec.seq_view_restriction_part
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE callrec.seq_view_restriction_part OWNER TO postgres;
GRANT UPDATE ON TABLE callrec.seq_view_restriction_part TO callrecgrp;
