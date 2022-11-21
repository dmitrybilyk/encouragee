CREATE SEQUENCE wbsc.seq_dataexport_status
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE wbsc.seq_dataexport_status
  OWNER TO postgres;
GRANT ALL ON SEQUENCE wbsc.seq_dataexport_status TO postgres;
GRANT SELECT, USAGE ON SEQUENCE wbsc.seq_dataexport_status TO wbscgrp;
