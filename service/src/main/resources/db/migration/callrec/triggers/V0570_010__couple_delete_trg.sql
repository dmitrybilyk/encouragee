CREATE OR REPLACE FUNCTION callrec.couple_deleted()
  RETURNS TRIGGER
VOLATILE
SECURITY DEFINER
AS $BODY$
    DECLARE

BEGIN
  IF callrec.track_changes_enabled() THEN
    INSERT into callrec.deleted_couples (sid)
    VALUES(OLD.sid);
  END IF;
  RETURN OLD;
END;
$BODY$
LANGUAGE 'plpgsql';


CREATE TRIGGER trig_on_couple_delete AFTER DELETE ON callrec.couples
FOR EACH ROW EXECUTE PROCEDURE callrec.couple_deleted();



