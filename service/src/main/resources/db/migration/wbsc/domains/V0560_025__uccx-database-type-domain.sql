ALTER DOMAIN wbsc.database_type_domain DROP CONSTRAINT database_type_switch;
ALTER DOMAIN wbsc.database_type_domain ADD CONSTRAINT database_type_switch CHECK (VALUE::text = ANY (ARRAY['LOCAL'::character varying::text, 'CALLREC'::character varying::text, 'IPCC'::character varying::text, 'GENESYS'::character varying::text, 'UCCX'::character varying::text]));
