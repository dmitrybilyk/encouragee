CREATE FUNCTION trig_cfiles_after_insert_speechrec() RETURNS TRIGGER AS $speechrec_trg$
BEGIN
  IF (is_speechrec() = true AND NEW.cftype = 'AUDIO' AND right(NEW.cfpath, 3) = 'wav') THEN
    INSERT INTO speechrec(cplid) VALUES (NEW.cplid);
  END IF;
  RETURN NULL;
END;
$speechrec_trg$ LANGUAGE plpgsql;

CREATE TRIGGER speechrec_trg AFTER INSERT ON cfiles
  FOR EACH ROW EXECUTE PROCEDURE trig_cfiles_after_insert_speechrec();
