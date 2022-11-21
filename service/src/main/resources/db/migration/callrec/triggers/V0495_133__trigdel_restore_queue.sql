-- $Id: trigdel_restore_queue.sql,v 1.2 2007-03-23 09:24:42 karl Exp $

CREATE TRIGGER trig_on_after_restore_queue_delete AFTER DELETE ON callrec.restore_queue
	FOR EACH ROW EXECUTE PROCEDURE callrec.on_after_restore_queue_delete();
