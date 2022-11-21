-- $Id

CREATE OR REPLACE FUNCTION callrec.on_after_restore_queue_insert()
RETURNS TRIGGER
AS $$
BEGIN
  UPDATE callrec.couples SET  restored='Q' WHERE id = NEW.cplid;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION callrec.on_after_restore_queue_insert() TO GROUP callrecgrp;
