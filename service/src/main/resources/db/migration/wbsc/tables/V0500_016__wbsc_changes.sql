
ALTER TABLE wbsc.subevaluation ALTER COLUMN interactiontypeid SET NOT NULL;

-----------------------------------------------------------------------
--------------------SPEECH REC-----------------------------------------
-----------------------------------------------------------------------

-- SC-3621
GRANT USAGE ON SCHEMA callrec TO group wbscgrp;
GRANT SELECT ON callrec.couples, callrec.couple_fixed_extdata, callrec.cfiles, callrec.extdata_map TO group wbscgrp;


GRANT SELECT, UPDATE ON TABLE voice_tags_seq TO wbscgrp;
GRANT SELECT, USAGE ON TABLE voice_tags_seq TO wbsc;
GRANT SELECT, USAGE ON TABLE voice_tags_seq TO callrec;
GRANT SELECT, UPDATE ON TABLE speech_tags_seq TO wbscgrp;
GRANT SELECT, USAGE ON TABLE speech_tags_seq TO wbsc;
GRANT SELECT, USAGE ON TABLE speech_tags_seq TO callrec;
GRANT SELECT, UPDATE ON TABLE speech_phrases_seq TO wbscgrp;
GRANT SELECT, USAGE ON TABLE speech_phrases_seq TO wbsc;
GRANT SELECT, USAGE ON TABLE speech_phrases_seq TO callrec;



ALTER DOMAIN wbsc.criteria_period_domain DROP CONSTRAINT answers_compliance_switch;
ALTER DOMAIN wbsc.criteria_period_domain  ADD  CONSTRAINT answers_compliance_switch CHECK (((VALUE)::text = ANY ((ARRAY['YESTERDAY'::character varying, 'LAST_WEEK'::character varying, 'THIS_WEEK'::character varying, 'LAST_MONTH'::character varying, 'THIS_MONTH'::character varying, 'NEXT_MONTH'::character varying, 'THIS_YEAR'::character varying, 'LAST_YEAR'::character varying, 'FIRST_QUARTER'::character varying, 'SECOND_QUARTER'::character varying, 'THIRD_QUARTER'::character varying, 'FOURTH_QUARTER'::character varying, 'THIS_QUARTER'::character varying, 'CUSTOM_LAST_WEEK'::character varying, 'CUSTOM_THIS_WEEK'::character varying, 'CUSTOM_NEXT_WEEK'::character varying, 'CUSTOM_LAST_MONTH'::character varying, 'CUSTOM_THIS_MONTH'::character varying, 'CUSTOM_NEXT_MONTH'::character varying, 'LAST_3_DAYS'::character varying,'LAST_2_WEEKS'::character varying,'LAST_6_MONTHS'::character varying ,'LAST_QUARTER'::character varying ])::text[])));

-- SC-4522

ALTER TABLE wbsc.questforms ALTER COLUMN time_from TYPE time without time zone;
ALTER TABLE wbsc.questforms ALTER COLUMN time_to TYPE time without time zone;



COMMENT ON COLUMN wbsc.subevaluation.length IS 'this field is filled only if the isn''t a call, otherwise length in evalcall is filled in.';

