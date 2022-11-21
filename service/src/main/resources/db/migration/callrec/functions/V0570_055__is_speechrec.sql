CREATE FUNCTION set_is_speechrec(IN enabled boolean) RETURNS VOID AS $$
BEGIN
  EXECUTE 'CREATE OR REPLACE FUNCTION callrec.is_speechrec()
             RETURNS boolean AS $INNER$
           BEGIN
             RETURN ' || enabled || ';
           END;
           $INNER$ LANGUAGE plpgsql IMMUTABLE;';
END;

$$ LANGUAGE plpgsql;
ALTER FUNCTION callrec.set_is_speechrec(IN boolean) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.set_is_speechrec(IN boolean) TO public;
GRANT EXECUTE ON FUNCTION callrec.set_is_speechrec(IN boolean) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.set_is_speechrec(IN boolean) TO callrecgrp;

SELECT set_is_speechrec(false);

ALTER FUNCTION callrec.is_speechrec() OWNER TO callrecgrp;
GRANT EXECUTE ON FUNCTION callrec.is_speechrec() TO public;
GRANT EXECUTE ON FUNCTION callrec.is_speechrec() TO postgres;
GRANT EXECUTE ON FUNCTION callrec.is_speechrec() TO callrecgrp;


SELECT CASE
  WHEN exists(SELECT 1
    FROM   pg_catalog.pg_class c
    JOIN   pg_catalog.pg_namespace n ON n.oid = c.relnamespace
    WHERE  n.nspname = 'callrec'
    AND    c.relname = 'speechrec'
    AND    c.relkind = 'r'
  )=true
    THEN set_is_speechrec(true)
END;
