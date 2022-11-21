CREATE DOMAIN wbsc.data_export_file_type_domain
  AS character varying(10)
  COLLATE pg_catalog."default"
  CONSTRAINT data_export_file_type_switch CHECK (VALUE::text = ANY (ARRAY['CSV'::character varying::text, 'XLSX'::character varying::text]));
ALTER DOMAIN wbsc.data_export_file_type_domain
  OWNER TO postgres;
