CREATE TRIGGER trigins
  AFTER INSERT ON callrec.couples
  FOR EACH ROW EXECUTE PROCEDURE callrec.TRIG_COUPLES_AFTER_INSERT();

