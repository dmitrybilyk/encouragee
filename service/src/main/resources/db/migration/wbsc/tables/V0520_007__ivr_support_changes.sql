-- SC-5363
-- questions
--   open-ended questions are allowed to have a free-form answer (for IVR it's an audio user comment)
ALTER TABLE wbsc.questions
  ADD COLUMN open_ended boolean NOT NULL DEFAULT false;

-- answers
--   1 unique key specified per answer (for IVR it must be a single digit 0 to 9 - checked in UI, not DB)
ALTER TABLE wbsc.answers
  ADD COLUMN answer_key character varying(50) NOT NULL DEFAULT 'NONE';

-- evalcalls
--   evalcall column couplesid should be nullable, IF external id and external id type are specified
ALTER TABLE wbsc.evalcalls
  ADD COLUMN external_id_type character varying(255);

ALTER TABLE wbsc.evalcalls
  ADD COLUMN external_id character varying(255);

ALTER TABLE wbsc.evalcalls
  ALTER COLUMN couplesid DROP NOT NULL;

ALTER TABLE wbsc.evalcalls ADD CONSTRAINT evalcalls_couplesid_ck CHECK (
  (couplesid IS NOT NULL)
  OR
  (external_id_type IS NOT NULL AND external_id IS NOT NULL)
);
