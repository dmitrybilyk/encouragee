-- $Id: trigupd_cfile.sql,v 1.2 2007-03-23 09:24:42 karl Exp $

CREATE TRIGGER trig_on_couple_cfile_update after UPDATE ON callrec.couples
	FOR EACH ROW EXECUTE PROCEDURE callrec.on_couple_cfile_update();
