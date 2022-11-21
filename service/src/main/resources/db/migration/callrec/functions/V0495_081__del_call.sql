-- CREATE FUNCTION DEL_CALL {{{
-- delete from calls
-- RI constraints make sure dependent couples, cfiles, etc get deleted too
CREATE OR REPLACE FUNCTION callrec.DEL_CALL(callrec.calls.id%TYPE)
  RETURNS BOOLEAN
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _id ALIAS FOR $1;
      _found BOOLEAN;
    BEGIN
      DELETE FROM callrec.calls WHERE id = _id;
      _found := FOUND;
      IF NOT FOUND THEN
        RAISE NOTICE ''No calls with id = %'', _id;
      END IF;
      -- couples get deleted through a refint constraint
      RETURN _found;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.DEL_CALL(callrec.calls.id%TYPE) TO GROUP callrecgrp;
-- }}}

