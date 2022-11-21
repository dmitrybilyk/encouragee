--return true if time is of expected day passed binnary

CREATE OR REPLACE  FUNCTION  callrec.get_bit_dow(start timestamp with time zone, expected bit(7))
  RETURNS boolean AS
$BODY$
    BEGIN
      RETURN ('0000001'::bit(7)<<extract(DOW FROM start)::integer)::bit(7)&expected::bit(7)!='0000000'::bit(7);
    END;
  $BODY$
  LANGUAGE 'plpgsql' IMMUTABLE;
ALTER FUNCTION callrec.get_bit_dow(start timestamp with time zone, expected bit(7)) OWNER TO postgres;
