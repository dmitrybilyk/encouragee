CREATE OR REPLACE FUNCTION callrec.couple_extdata_delete()
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


CREATE TRIGGER trig_on_couple_extdata_delete AFTER DELETE ON callrec.couple_extdata
FOR EACH ROW EXECUTE PROCEDURE callrec.couple_extdata_delete();

CREATE OR REPLACE FUNCTION callrec.trig_couples_ext_after_insert()
  RETURNS TRIGGER AS
  $BODY$
    DECLARE
      _id_col integer;
    BEGIN
      IF callrec.track_changes_enabled() THEN
        UPDATE callrec.couples
        SET dirty = true
        WHERE id = NEW.cplid;
      END IF;

      SELECT INTO _id_col columnid
      FROM callrec.extdata_map
      WHERE "key"=NEW.key;

      IF _id_col IS NOT NULL THEN
        PERFORM insert_fixed_extdata(NEW.key, NEW.value, NEW.cplid);
      END IF;

      RETURN NEW;
    END;
  $BODY$
LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;
ALTER FUNCTION callrec.trig_couples_ext_after_insert()
OWNER TO postgres;
