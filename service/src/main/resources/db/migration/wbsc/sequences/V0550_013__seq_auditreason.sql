CREATE SEQUENCE wbsc.seq_auditreason
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;

GRANT SELECT, USAGE ON SEQUENCE wbsc.seq_auditreason TO wbscgrp;
