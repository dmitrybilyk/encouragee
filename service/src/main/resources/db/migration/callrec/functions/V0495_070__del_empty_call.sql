-- CREATE FUNCTION _DEL_EMPTY_CALL {{{
-- delete a call if a given couple is its last
CREATE OR REPLACE FUNCTION callrec._DEL_EMPTY_CALL(callrec.couples.id%TYPE)
  RETURNS BOOLEAN
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _cplid ALIAS FOR $1;
    BEGIN
      DELETE FROM callrec.calls
      USING callrec.couples
      WHERE callrec.calls.id = callrec.couples.callid
        AND callrec.couples.id = _cplid
        AND realcplcnt = 1;
      RETURN FOUND;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec._DEL_EMPTY_CALL(callrec.couples.id%TYPE) TO GROUP callrecgrp;
-- }}}

