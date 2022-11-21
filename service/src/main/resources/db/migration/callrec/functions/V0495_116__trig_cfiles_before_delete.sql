CREATE OR REPLACE FUNCTION callrec.trig_cfiles_before_delete()
  RETURNS trigger AS
$BODY$
      BEGIN

      PERFORM id
        FROM callrec.couples C
        WHERE OLD.cftype NOT IN ('RECD')
        AND C.id = OLD.cplid
        AND C.protected= true;

      IF FOUND THEN
        RAISE EXCEPTION 'Cannot delete cfile %, couple % is protected against deleting.', OLD.id, OLD.cplid;
        RETURN NULL;
      END IF;

      RETURN OLD;
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;
ALTER FUNCTION callrec.trig_cfiles_before_delete() OWNER TO postgres;
