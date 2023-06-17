CREATE OR REPLACE FUNCTION get_version()
  RETURNS character varying AS
$BODY$
    BEGIN
      RETURN '5.3.1';
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION get_version() OWNER TO postgres;
