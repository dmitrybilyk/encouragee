-- SC-5363
CREATE SEQUENCE wbsc.seq_smediafiles
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

GRANT select, usage on sequence wbsc.seq_smediafiles to wbscgrp;
