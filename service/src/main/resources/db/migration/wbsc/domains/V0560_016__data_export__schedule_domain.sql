CREATE DOMAIN wbsc.data_export_schedule_domain
  AS character varying(50)
  COLLATE pg_catalog."default"
  CONSTRAINT data_export_schedule_switch CHECK (VALUE::text = ANY (ARRAY['SINGLE_INSTANCE'::character varying::text, 'RECURRING'::character varying::text]));
ALTER DOMAIN wbsc.data_export_schedule_domain
  OWNER TO postgres;
