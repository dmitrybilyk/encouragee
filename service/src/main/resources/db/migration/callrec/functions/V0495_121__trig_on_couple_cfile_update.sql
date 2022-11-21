-- $Id: trig_on_couple_cfile_update.sql,v 1.3 2007-03-23 09:24:40 karl Exp $

CREATE OR REPLACE FUNCTION callrec.on_couple_cfile_update()
RETURNS TRIGGER
AS $$
BEGIN
	-- Check that empname and salary are given
	IF new.cfcnt != old.cfcnt AND (NEW.cfcnt IS NULL OR NEW.cfcnt <= 0) THEN
		DELETE FROM callrec.restore_queue
      WHERE cplid = NEW.id AND callid = NEW.callid;
	END IF;
	RETURN NEW;
END;
$$ language plpgsql;

GRANT EXECUTE ON FUNCTION callrec.on_couple_cfile_update() TO GROUP callrecgrp;
