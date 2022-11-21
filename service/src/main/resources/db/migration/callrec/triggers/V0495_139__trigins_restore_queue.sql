-- $Id

CREATE TRIGGER trig_on_after_restore_queue_insert AFTER INSERT ON callrec.restore_queue
	FOR EACH ROW EXECUTE PROCEDURE callrec.on_after_restore_queue_insert();
