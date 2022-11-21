-- Table: wbsc.criteria

-- DROP TABLE wbsc.criteria;

CREATE TABLE wbsc.criteria
(
  criteriaid integer NOT NULL DEFAULT nextval(('wbsc.seq_criteria'::text)::regclass),
  evaluationid integer,
  is_main boolean NOT NULL, -- if true, this record represent basic settings of evaluation
  period_from timestamp with time zone,
  period_to timestamp with time zone,
  total_events integer, -- calls, videos, emails....
  call_dir_type wbsc.criteria_call_directions_domain NOT NULL, -- incomming out outgoing
  call_wrapup integer,
  minimal_length integer, -- length of call
  maximal_length integer, -- length of call
  daytime_from time with time zone,
  daytime_to time with time zone,
  weekdays bit varying(7),
  allow_call_selection boolean NOT NULL, -- if true, selection of a specific call is allowed
  allow_call_replacement boolean NOT NULL, -- if true, a supervisor is allwoed to get new random calls.
  finish_subcriteria_first boolean NOT NULL, -- if true, a supervisor needs to evaluate calls from this set before moving on.
  deadline timestamp with time zone,
  reminder_days integer, -- days prior remainding
  reminder_date timestamp with time zone, -- remainding date
  reminded_on timestamp(0) with time zone, -- when this event was remainded to the user.
  ordering integer NOT NULL,
  random_order boolean NOT NULL,
  description text,
  descr_comparator wbsc.externaldata_comparator_domain,
  period wbsc.criteria_period_domain,
  allow_call_fillingup BOOLEAN,
  CONSTRAINT subcriteria_pkey PRIMARY KEY (criteriaid),
  CONSTRAINT callwrapups_fk FOREIGN KEY (call_wrapup)
      REFERENCES wbsc.callwrapups (callwrapupid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT evaluations_fk FOREIGN KEY (evaluationid)
      REFERENCES wbsc.evaluations (evaluationid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.criteria OWNER TO postgres;
COMMENT ON COLUMN wbsc.criteria.is_main IS 'if true, this record represent basic settings of evaluation';
COMMENT ON COLUMN wbsc.criteria.total_events IS 'calls, videos, emails....';
COMMENT ON COLUMN wbsc.criteria.call_dir_type IS 'incomming out outgoing';
COMMENT ON COLUMN wbsc.criteria.minimal_length IS 'length of call';
COMMENT ON COLUMN wbsc.criteria.maximal_length IS 'length of call';
COMMENT ON COLUMN wbsc.criteria.allow_call_selection IS 'if true, selection of a specific call is allowed';
COMMENT ON COLUMN wbsc.criteria.allow_call_replacement IS 'if true, a supervisor is allwoed to get new random calls.';
COMMENT ON COLUMN wbsc.criteria.finish_subcriteria_first IS 'if true, a supervisor needs to evaluate calls from this set before moving on.';
COMMENT ON COLUMN wbsc.criteria.reminder_days IS 'days prior remainding';
COMMENT ON COLUMN wbsc.criteria.reminder_date IS 'remainding date';
COMMENT ON COLUMN wbsc.criteria.reminded_on IS 'when this event was remainded to the user.';
COMMENT ON COLUMN wbsc.criteria.allow_call_fillingup IS 'If enabled, then Get Random calls shall continue with Basic criteria if not enough calls matched Sub-Criteria.If disabled, then Get Random calls shall  not continue and only calls matching Sub-criteria shall be added into the evaluation.';

GRANT select, update, insert, delete on wbsc.criteria to wbscgrp;

