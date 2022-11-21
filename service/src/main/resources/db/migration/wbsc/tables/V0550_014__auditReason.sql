CREATE TABLE wbsc.auditReason
(
  auditReasonId integer NOT NULL DEFAULT nextval('wbsc.seq_auditreason'::regclass),
  value character varying(255) NOT NULL,
  companyId integer NOT NULL,
  visible boolean NOT NULL DEFAULT TRUE,
  CONSTRAINT auditReason_pkey PRIMARY KEY (auditReasonId),
  CONSTRAINT auditReason_companyId_fk FOREIGN KEY (companyId)
    REFERENCES wbsc.companies (companyId) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE SET NULL
)
WITHOUT OIDS;

ALTER TABLE wbsc.auditReason
  OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.auditReason TO wbscgrp;

ALTER TABLE wbsc.audit
  ADD COLUMN reasonId INTEGER,
  ADD CONSTRAINT audit_reasonId_fk FOREIGN KEY (reasonId)
      REFERENCES wbsc.auditReason (auditReasonId) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
