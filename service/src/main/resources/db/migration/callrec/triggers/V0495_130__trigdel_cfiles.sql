CREATE TRIGGER trigdel
  AFTER DELETE ON callrec.cfiles
 FOR EACH ROW EXECUTE PROCEDURE callrec.TRIG_CFILES_AFTER_DELETE();

CREATE TRIGGER trig_before_del
  BEFORE DELETE
  ON callrec.cfiles
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.trig_cfiles_before_delete();

