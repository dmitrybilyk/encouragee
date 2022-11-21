-- Table: wbsc.evalcalls

-- DROP TABLE wbsc.evalcalls;

CREATE TABLE wbsc.evalcalls
(
  couplesid character varying(255) NOT NULL,
  start_date timestamp(0) with time zone NOT NULL,
  from_number character varying(50),
  to_number character varying(50),
  length integer,
  interactiontypeid integer NOT NULL,
  stop_date timestamp with time zone,
  subevaluationid integer NOT NULL,
  is_main_call boolean,
  evalcallsid integer NOT NULL DEFAULT nextval('wbsc.seq_evalcalls'::regclass),
  CONSTRAINT evalcalls_pkey PRIMARY KEY (evalcallsid),
  CONSTRAINT evalcalls_fk FOREIGN KEY (subevaluationid)
      REFERENCES wbsc.subevaluation (subevaluationid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT sc_evalcalls_interactiontype_fk FOREIGN KEY (interactiontypeid)
      REFERENCES wbsc.interaction_types (interactiontypeid) MATCH SIMPLE
	  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITHOUT OIDS;
ALTER TABLE wbsc.evalcalls OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.evalcalls to wbscgrp;
