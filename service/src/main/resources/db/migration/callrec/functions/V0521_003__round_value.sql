CREATE OR REPLACE FUNCTION roundvalue(real)
  RETURNS double precision AS
$BODY$
BEGIN
    RETURN round(CAST($1 as numeric),6);
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION get_version() OWNER TO postgres;

