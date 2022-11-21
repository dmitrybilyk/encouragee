CREATE SEQUENCE wbsc.seq_calibrated_interactions
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

ALTER TABLE wbsc.seq_calibrated_interactions OWNER TO postgres;
GRANT ALL ON SEQUENCE wbsc.seq_calibrated_interactions to wbscgrp;
