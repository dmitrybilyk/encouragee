-- Create scoring tiers table
CREATE TABLE wbsc.scoring_tiers
(
  tier_id integer NOT NULL DEFAULT nextval('wbsc.seq_scoring_tiers'::regclass),
  tier_name character varying(255) NOT NULL,
  description character varying(255) NOT NULL,
  scoring_tier_type character varying(255),
  other_color character varying(255),
  other_display VARCHAR (255),
  CONSTRAINT scoring_tiers_pkey PRIMARY KEY (tier_id)
)
WITHOUT OIDS;

ALTER TABLE wbsc.scoring_tiers
OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.scoring_tiers TO wbscgrp;

-- Create scoring tier values table
CREATE TABLE wbsc.scoring_tier_values
(
  tier_value_id integer NOT NULL DEFAULT nextval('wbsc.seq_scoring_tier_values'::regclass),
  value_to NUMERIC(12,6),
  color VARCHAR (255),
  display VARCHAR (255),
  scoring_tier_id integer,
  CONSTRAINT scoring_tier_values_pkey PRIMARY KEY (tier_value_id),
  CONSTRAINT scoring_tier_values_scoring_tierId_fk FOREIGN KEY (scoring_tier_id)
  REFERENCES wbsc.scoring_tiers (tier_id) MATCH SIMPLE
  ON UPDATE CASCADE ON DELETE SET NULL
)
WITHOUT OIDS;

ALTER TABLE wbsc.scoring_tier_values
OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.scoring_tier_values TO wbscgrp;
