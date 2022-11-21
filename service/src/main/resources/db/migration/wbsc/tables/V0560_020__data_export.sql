
CREATE TABLE wbsc.data_export
(
  dataexportid integer NOT NULL DEFAULT nextval('seq_dataexport'::regclass),
  name character varying(255) NOT NULL,
  created_by integer NOT NULL, --owner of data export task
  last_run timestamp with time zone, --last execution time
  next_run timestamp with time zone, --next execution time
  recurring_type data_export_recurring_domain, --rule for calculation of next run
  schedule_type data_export_schedule_domain NOT NULL,
  store_type data_export_store_domain NOT NULL,
  filename character varying(255), --name of created export file
  file_path character varying(500),
  expiration_period integer,
  append_timestamp boolean default false,
  email text, --mail to: list of all emails that should be recipients
  qformid integer,  --questionnaire id

  include_attached_data boolean default false,
  evaluationid_from integer,
  evaluationid_to integer,
  evaluation_score_from numeric(12,6),
  evaluation_score_to numeric(12,6),
  subevaluation_score_from numeric(12,6),
  subevaluation_score_to numeric(12,6),
  call_start_date_from timestamp with time zone,
  call_start_date_to timestamp with time zone,
  evaluation_period_from timestamp with time zone,
  evaluation_period_to timestamp with time zone,
  evaluation_date_from timestamp with time zone,
  evaluation_date_to timestamp with time zone,
  agentid integer, --evaluated agent
  evaluatorid integer, --evaluator
  ccgroupid integer, --group

  CONSTRAINT data_export_pkey PRIMARY KEY (dataexportid),

  CONSTRAINT data_export_created_by FOREIGN KEY (created_by)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT questforms_fk FOREIGN KEY (qformid)
      REFERENCES wbsc.questforms (qformid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT data_export_agentid FOREIGN KEY (agentid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT data_export_evaluatorid FOREIGN KEY (evaluatorid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT data_export_groupid FOREIGN KEY (ccgroupid)
      REFERENCES wbsc.ccgroups (ccgroupid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wbsc.data_export
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.data_export TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.data_export TO wbscgrp;
