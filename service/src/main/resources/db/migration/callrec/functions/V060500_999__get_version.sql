CREATE OR REPLACE FUNCTION get_version()
  RETURNS character varying AS
$BODY$
  DECLARE
    vers character varying(50):='0.0.0';
    maxversion character varying(50);
  BEGIN
    SELECT max(version) INTO maxversion from callrec."SCHEMA_UPDATES";
    vers:=SUBSTRING(maxversion from 1 for 2)::Integer || '.' || SUBSTRING(maxversion from 3 for 2)::Integer || '.' || SUBSTRING(maxversion from 5 for 2)::Integer;
    RETURN vers;
  END
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION get_version() OWNER TO postgres;
