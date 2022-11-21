CREATE DOMAIN wbsc.data_export_recurring_domain
  AS character varying(50)
  COLLATE pg_catalog."default"
  CONSTRAINT data_export_recurring_switch CHECK (VALUE::text = ANY (ARRAY['DAILY'::character varying::text, 'WEEKLY'::character varying::text, 'MONTHLY'::character varying::text, 'QUARTERLY'::character varying::text, 'YEARLY'::character varying::text, 'CUSTOM_WEEKLY'::character varying::text, 'CUSTOM_MONTHLY'::character varying::text, 'CUSTOM_QUARTERLY'::character varying::text, 'CUSTOM_YEARLY'::character varying::text]));
ALTER DOMAIN wbsc.data_export_recurring_domain
  OWNER TO postgres;
