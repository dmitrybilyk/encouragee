-- Table: wbsc.audit

-- DROP TABLE wbsc.audit;

CREATE TABLE wbsc.audit
(
  auditid integer NOT NULL DEFAULT nextval('wbsc.seq_audit'::regclass),
  datetime timestamp with time zone NOT NULL,
  "type" character varying(255) NOT NULL, -- type of event
  logged_user integer,
  result character varying(255) NOT NULL,
  parameters text, -- value seperated list of values needed to reconstruct a localized message
  CONSTRAINT audit_pkey PRIMARY KEY (auditid),
  CONSTRAINT audit_user_fk FOREIGN KEY (logged_user)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL
)
WITHOUT OIDS;
ALTER TABLE wbsc.audit OWNER TO postgres;
COMMENT ON COLUMN wbsc.audit."type" IS 'type of event';
COMMENT ON COLUMN wbsc.audit.parameters IS 'value seperated list of values needed to reconstruct a localized message';

GRANT select, update, insert, delete on wbsc.audit to wbscgrp;

