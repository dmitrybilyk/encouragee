CREATE SEQUENCE wbsc.seq_criteria_to_surveys
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

ALTER SEQUENCE wbsc.seq_criteria_to_surveys OWNER TO postgres;
GRANT ALL ON SEQUENCE wbsc.seq_criteria_to_surveys  TO postgres;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE wbsc.seq_criteria_to_surveys  TO wbscgrp;
