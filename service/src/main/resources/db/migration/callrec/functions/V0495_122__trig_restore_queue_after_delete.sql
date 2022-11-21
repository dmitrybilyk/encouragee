-- $Id

CREATE OR REPLACE FUNCTION callrec.on_after_restore_queue_delete()
RETURNS TRIGGER
AS $$
BEGIN
UPDATE
	callrec.couples
SET
	restored = null
WHERE
	id = OLD.cplid;
RETURN OLD;
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION callrec.on_after_restore_queue_delete() TO GROUP callrecgrp;
