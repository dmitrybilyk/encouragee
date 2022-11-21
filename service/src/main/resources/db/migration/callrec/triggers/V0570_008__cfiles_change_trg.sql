
CREATE OR REPLACE FUNCTION callrec.cfiles_update_delete()
  RETURNS TRIGGER
VOLATILE
SECURITY DEFINER
AS $BODY$
BEGIN
  IF callrec.track_changes_enabled() THEN
    UPDATE callrec.couples
    SET dirty = true
    WHERE id = OLD.cplid;
  END IF;
  RETURN OLD;
END;
$BODY$
LANGUAGE 'plpgsql';


CREATE TRIGGER trig_on_cfile_update_delete AFTER UPDATE OR DELETE ON callrec.cfiles
FOR EACH ROW EXECUTE PROCEDURE callrec.cfiles_update_delete();


CREATE OR REPLACE FUNCTION callrec.trig_cfiles_after_insert()
  RETURNS TRIGGER
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _empty BIT(32);
    BEGIN
      _empty := CAST(0 AS BIT(32));

      UPDATE callrec.couples
        SET
          cftypes = COALESCE(cftypes, _empty) | callrec.GET_CFTYPEMASK(NEW.cftype),
          cfcnt = COALESCE(cfcnt, 0) + 1
        WHERE
          id = NEW.cplid;

      IF callrec.track_changes_enabled() THEN
        UPDATE callrec.couples
        SET dirty = true
        WHERE id = NEW.cplid;
      END IF;

      RETURN NULL;
    END;
  ';


