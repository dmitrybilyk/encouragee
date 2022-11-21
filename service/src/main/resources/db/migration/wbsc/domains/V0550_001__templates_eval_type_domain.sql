CREATE DOMAIN wbsc.evaluation_type AS character varying(50) CONSTRAINT evaluation_type_switch CHECK (((VALUE)::text = ANY ((ARRAY['QUALITY'::character varying, 'TRAINING'::character varying, 'SURVEY'::character varying])::text[])));
