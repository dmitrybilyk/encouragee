-- Table: wbsc.medialimits

-- DROP TABLE wbsc.medialimits;

CREATE TABLE wbsc.medialimits
(
  medialimitid integer NOT NULL DEFAULT nextval(('wbsc.seq_medialimits'::text)::regclass),
  criteriaid integer NOT NULL,
  interactiontypeid integer NOT NULL,
  minimum integer NOT NULL,
  CONSTRAINT events_pkey PRIMARY KEY (medialimitid),
  CONSTRAINT events_subcriteria_fk FOREIGN KEY (criteriaid)
      REFERENCES wbsc.criteria (criteriaid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT sc_medialimits_interactiontype_fk FOREIGN KEY (interactiontypeid)
      REFERENCES wbsc.interaction_types (interactiontypeid) MATCH SIMPLE
	  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITHOUT OIDS;
ALTER TABLE wbsc.medialimits OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.medialimits to wbscgrp;
