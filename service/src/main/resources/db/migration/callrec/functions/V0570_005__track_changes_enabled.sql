CREATE OR REPLACE FUNCTION callrec.set_track_changes_enabled(IN enabled boolean)
  RETURNS VOID AS $$
BEGIN
  EXECUTE 'CREATE OR REPLACE FUNCTION callrec.track_changes_enabled()
             RETURNS boolean AS $INNER$
           BEGIN
             RETURN ' || enabled || ';
           END;
           $INNER$ LANGUAGE plpgsql IMMUTABLE;';
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION callrec.set_track_changes_enabled(IN boolean) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.set_track_changes_enabled(IN boolean) TO public;
GRANT EXECUTE ON FUNCTION callrec.set_track_changes_enabled(IN boolean) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.set_track_changes_enabled(IN boolean) TO callrecgrp;

SELECT set_track_changes_enabled(false);

ALTER FUNCTION callrec.track_changes_enabled() OWNER TO callrecgrp;
GRANT EXECUTE ON FUNCTION callrec.track_changes_enabled() TO public;
GRANT EXECUTE ON FUNCTION callrec.track_changes_enabled() TO postgres;
GRANT EXECUTE ON FUNCTION callrec.track_changes_enabled() TO callrecgrp;
