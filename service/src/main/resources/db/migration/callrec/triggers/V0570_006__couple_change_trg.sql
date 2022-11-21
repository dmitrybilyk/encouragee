CREATE OR REPLACE FUNCTION callrec.couple_updated()
  RETURNS TRIGGER
VOLATILE
SECURITY DEFINER
AS $BODY$
BEGIN
  IF (callrec.track_changes_enabled() AND NOT OLD.dirty AND NOT NEW.dirty) THEN
    NEW.dirty = true;
  END IF;
  RETURN NEW;
END;
$BODY$
LANGUAGE 'plpgsql';


CREATE TRIGGER trig_on_couple_change BEFORE UPDATE ON callrec.couples
FOR EACH ROW EXECUTE PROCEDURE callrec.couple_updated();
