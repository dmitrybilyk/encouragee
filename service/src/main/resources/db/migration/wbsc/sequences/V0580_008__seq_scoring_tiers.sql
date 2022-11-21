-- Create scoring tiers sequence
CREATE SEQUENCE wbsc.seq_scoring_tiers
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;
GRANT SELECT, USAGE ON SEQUENCE wbsc.seq_scoring_tiers TO wbscgrp;

-- Create scoring tier values sequence
CREATE SEQUENCE wbsc.seq_scoring_tier_values
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;
GRANT SELECT, USAGE ON SEQUENCE wbsc.seq_scoring_tier_values TO wbscgrp;
