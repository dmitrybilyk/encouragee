CREATE DOMAIN wbsc.data_export_store_domain
  AS character varying(50)
  COLLATE pg_catalog."default"
  CONSTRAINT data_export_store_switch CHECK (VALUE::text = ANY (ARRAY['FILE'::character varying::text, 'EMAIL'::character varying::text]));
ALTER DOMAIN wbsc.data_export_store_domain
  OWNER TO postgres;
