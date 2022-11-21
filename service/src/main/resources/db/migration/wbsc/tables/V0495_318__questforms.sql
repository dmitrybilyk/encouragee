-- Table: wbsc.questforms

-- DROP TABLE wbsc.questforms;

CREATE TABLE wbsc.questforms
(
  qformid integer NOT NULL DEFAULT nextval('wbsc.seq_questforms'::regclass),
  qformname character varying(100) NOT NULL,
  "version" character varying(50),
  accessibility wbsc.questionform_access_domain NOT NULL,
  scoring_system wbsc.questionform_scoring_system_domain NOT NULL, -- percentage, points or marks?
  detailed_report boolean NOT NULL DEFAULT true,
  sorting wbsc.questionform_sorting_domain NOT NULL,
  exact_numbers boolean NOT NULL DEFAULT true,
  sipnumber boolean NOT NULL DEFAULT false,
  extension_length integer,
  internal_calls_scoring boolean NOT NULL DEFAULT false,
  matching_part wbsc.questionform_matchingparts_domain NOT NULL,
  weekdays bit varying(7) NOT NULL,
  time_from time without time zone,
  time_to time without time zone,
  min_call_length integer,
  max_call_length integer,
  description text,
  company integer NOT NULL,
  completed boolean NOT NULL,
  report_weight real NOT NULL DEFAULT 1,
  CONSTRAINT questforms_pkey PRIMARY KEY (qformid),
  CONSTRAINT questforms_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT questforms_qformname_key UNIQUE (qformname, version)
)
WITHOUT OIDS;
ALTER TABLE wbsc.questforms OWNER TO postgres;
COMMENT ON COLUMN wbsc.questforms.scoring_system IS 'percentage, points or marks?';

GRANT select, update, insert, delete on wbsc.questforms to wbscgrp;

