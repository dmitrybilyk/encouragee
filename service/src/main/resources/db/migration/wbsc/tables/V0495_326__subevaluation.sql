-- Table: wbsc.subevaluation

-- DROP TABLE wbsc.subevaluation;

CREATE TABLE wbsc.subevaluation
(
  subevaluationid integer NOT NULL DEFAULT nextval('wbsc.seq_subevaluations'::regclass),
  criteriaid integer NOT NULL,
  ordering integer,
  ticket_number character varying(80),
  mail_number character varying(80),

  categoryid integer,
  sub_eval_total real,
  feedback_improve text,
  feedback_maintain text,
  internal_note text,
  fast_note text,
  length integer, -- this field is filled only if the isn't a call, otherwise length in evalcall is filled in.
  interactiontypeid integer NOT NULL,
  replaced boolean NOT NULL,
  subevalstatus "wbsc"."subevaluation_subeval_status_domain" NOT NULL,
  "location" character varying(255),
  CONSTRAINT subevaluation_pkey PRIMARY KEY (subevaluationid),
  CONSTRAINT categories_fk FOREIGN KEY (categoryid)
      REFERENCES wbsc.categories (categoryid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT subcriteria_fk FOREIGN KEY (criteriaid)
      REFERENCES wbsc.criteria (criteriaid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT sc_subevaluation_interactiontype_fk FOREIGN KEY (interactiontypeid)
      REFERENCES wbsc.interaction_types (interactiontypeid) MATCH SIMPLE
	  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITHOUT OIDS;
ALTER TABLE wbsc.subevaluation OWNER TO postgres;
COMMENT ON TABLE wbsc.subevaluation IS 'evaluation of a single call';
COMMENT ON COLUMN wbsc.subevaluation.length IS 'this field is filled only if the isn''t a call, otherwise length in evalcall is filled in.';

GRANT select, update, insert, delete on wbsc.subevaluation to wbscgrp;

