CREATE OR REPLACE FUNCTION callrec.voice_tags_updated_deleted()
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

CREATE OR REPLACE FUNCTION callrec.voice_tags_inserted()
  RETURNS TRIGGER
VOLATILE
SECURITY DEFINER
AS $BODY$
BEGIN
  IF callrec.track_changes_enabled() THEN
    UPDATE callrec.couples
    SET dirty = true
    WHERE id = NEW.cplid;
  END IF;
  RETURN NEW;
END;
$BODY$
LANGUAGE 'plpgsql';