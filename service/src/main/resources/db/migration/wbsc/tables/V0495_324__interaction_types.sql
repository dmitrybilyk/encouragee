CREATE TABLE wbsc.interaction_types
(
  interactiontypeid integer NOT NULL DEFAULT nextval('wbsc.seq_interactiontypes'::regclass),
  "name" character varying(20) NOT NULL,
  "type" wbsc.interaction_type_domain NOT NULL,
  company integer NOT NULL,
  CONSTRAINT interactiontype_pkey PRIMARY KEY (interactiontypeid),
  CONSTRAINT sc_interactiontypes_name UNIQUE (name, company)
)
WITHOUT OIDS;

ALTER TABLE wbsc.interaction_types OWNER TO postgres;

CREATE INDEX sc_interactiontypes_company
  ON wbsc.interaction_types
  USING btree
  (company);

GRANT select, update, insert, delete on wbsc.interaction_types to wbscgrp;
