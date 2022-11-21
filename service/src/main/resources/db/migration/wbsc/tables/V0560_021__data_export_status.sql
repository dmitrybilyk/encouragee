CREATE TABLE wbsc.data_export_status
(
  dataexportstatusid integer NOT NULL DEFAULT nextval('seq_dataexport_status'::regclass),
  dataexportid  integer NOT NULL, --FK
  status evaluations_eval_status_domain NOT NULL,

  CONSTRAINT data_export_status_pkey PRIMARY KEY (dataexportstatusid),
  CONSTRAINT data_export_fk FOREIGN KEY (dataexportid)
      REFERENCES wbsc.data_export (dataexportid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wbsc.data_export_status
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.data_export_status TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.data_export_status TO wbscgrp;
