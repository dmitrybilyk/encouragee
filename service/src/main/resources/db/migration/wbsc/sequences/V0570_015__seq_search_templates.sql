CREATE SEQUENCE wbsc.seq_searchtemplates
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE wbsc.seq_searchtemplates
  OWNER TO postgres;
GRANT ALL ON SEQUENCE wbsc.seq_searchtemplates TO postgres;
GRANT SELECT, USAGE ON SEQUENCE wbsc.seq_searchtemplates TO wbscgrp;
