-- CREATE FUNCTION TRIG_COUPLES_AFTER_DELETE {{{
-- decrement calls.realcplcnt when deleting a couple
CREATE OR REPLACE FUNCTION callrec.TRIG_COUPLES_AFTER_DELETE()
  RETURNS TRIGGER
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    BEGIN
      UPDATE callrec.calls
        SET
          realcplcnt = realcplcnt - 1
        WHERE
          id = OLD.callid;
      RETURN NULL;
    END;
  ';
-- }}}

