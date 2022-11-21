  CREATE SEQUENCE wbsc.seq_criteria_to_speech_tags
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

  ALTER SEQUENCE wbsc.seq_criteria_to_speech_tags OWNER TO postgres;
  GRANT ALL ON SEQUENCE wbsc.seq_criteria_to_speech_tags  TO postgres;
  GRANT SELECT, UPDATE, USAGE ON SEQUENCE wbsc.seq_criteria_to_speech_tags  TO wbscgrp;
