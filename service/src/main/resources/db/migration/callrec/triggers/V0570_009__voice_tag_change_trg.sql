
CREATE OR REPLACE FUNCTION callrec.voice_tags_updated_deleted()
  RETURNS TRIGGER
VOLATILE
SECURITY DEFINER
AS $BODY$
BEGIN
  IF callrec.track_changes_enabled() THEN
    UPDATE callrec.couples
    SET dirty = true
    WHERE id = (SELECT cplid FROM callrec.cfiles WHERE id=OLD.cfileid);
  END IF;
  RETURN OLD;
END;
$BODY$
LANGUAGE 'plpgsql';


CREATE TRIGGER trig_on_voice_tags_update_delete AFTER UPDATE OR DELETE ON callrec.voice_tags
FOR EACH ROW EXECUTE PROCEDURE callrec.voice_tags_updated_deleted();



CREATE OR REPLACE FUNCTION callrec.voice_tags_inserted()
  RETURNS TRIGGER
VOLATILE
SECURITY DEFINER
AS $BODY$
BEGIN
  IF callrec.track_changes_enabled() THEN
    UPDATE callrec.couples
    SET dirty = true
    WHERE id = (SELECT cplid FROM callrec.cfiles WHERE id=NEW.cfileid);
  END IF;
  RETURN NEW;
END;
$BODY$
LANGUAGE 'plpgsql';


CREATE TRIGGER trig_on_voice_tags_insert AFTER INSERT ON callrec.voice_tags
FOR EACH ROW EXECUTE PROCEDURE callrec.voice_tags_inserted();
