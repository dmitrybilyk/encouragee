CREATE OR REPLACE FUNCTION callrec.couple_updated()
  RETURNS TRIGGER
VOLATILE
SECURITY DEFINER
AS $BODY$
BEGIN
  -- set to dirty if the change does not modify updated_ts correctly
  IF (callrec.track_changes_enabled()
    AND (NEW.updated_ts IS NULL OR NEW.updated_ts = OLD.updated_ts)
  ) THEN
    NEW.dirty = true;
  END IF;

  RETURN NEW;
END;
$BODY$
LANGUAGE 'plpgsql';
