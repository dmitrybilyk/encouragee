-- CREATE FUNCTION TRIG_COUPLES_BEFORE_DELETE {{{
-- check if call or couple is not protected agains deleting
CREATE OR REPLACE FUNCTION callrec.TRIG_COUPLES_BEFORE_DELETE()
  RETURNS TRIGGER
  VOLATILE
  SECURITY DEFINER
  AS $BODY$
    BEGIN
      IF OLD.protected = TRUE THEN
        RAISE EXCEPTION 'Couple % is protected against deleting', OLD.id;
      END IF;

      RETURN OLD;
    END;
  $BODY$
    LANGUAGE 'plpgsql';
-- }}}

