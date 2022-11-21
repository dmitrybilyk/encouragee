-- Table: wbsc.evaluations

-- DROP TABLE wbsc.evaluations;

CREATE TABLE wbsc.evaluations
(
  evaluationid integer NOT NULL DEFAULT nextval('wbsc.seq_evaluations'::regclass),
  evaluation_date timestamp with time zone, -- completion date
  qformid integer,
  evaluatorid integer, -- who will evaluate
  evaluated_user integer, -- whom will be evaluated
  allow_modification boolean NOT NULL, -- allows modification after this evaluation has been created
  last_modified timestamp with time zone NOT NULL,
  evalstatus wbsc.evaluations_eval_status_domain NOT NULL,
  comments text,
  copyof integer, -- if there is a need to evaluate the same calls by multiple evaluators, for example a training
  "read" boolean NOT NULL, -- if the agent has opened this evaluation at least once.
  feedback_improve text,
  feedback_maintain text,
  apply_to_statistics boolean,
  company integer NOT NULL,
  created_by integer,
  score real,
  visible_to_agent boolean DEFAULT true,
  CONSTRAINT evaluations_pkey PRIMARY KEY (evaluationid),
  CONSTRAINT evaluations_company_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT evaluations_created_by FOREIGN KEY (created_by)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT evaluations_fk FOREIGN KEY (copyof)
      REFERENCES wbsc.evaluations (evaluationid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT questforms_fk FOREIGN KEY (qformid)
      REFERENCES wbsc.questforms (qformid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT users_evaluated_fk FOREIGN KEY (evaluated_user)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT users_evaluatorid_fk FOREIGN KEY (evaluatorid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.evaluations OWNER TO postgres;
COMMENT ON COLUMN wbsc.evaluations.evaluation_date IS 'completion date';
COMMENT ON COLUMN wbsc.evaluations.evaluatorid IS 'who will evaluate';
COMMENT ON COLUMN wbsc.evaluations.evaluated_user IS 'whom will be evaluated';
COMMENT ON COLUMN wbsc.evaluations.allow_modification IS 'allows modification after this evaluation has been created';
COMMENT ON COLUMN wbsc.evaluations.copyof IS 'if there is a need to evaluate the same calls by multiple evaluators, for example a training';
COMMENT ON COLUMN wbsc.evaluations."read" IS 'if the agent has opened this evaluation at least once.';

GRANT select, update, insert, delete on wbsc.evaluations to wbscgrp;

