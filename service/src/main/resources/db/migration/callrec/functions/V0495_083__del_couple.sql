-- CREATE FUNCTION DEL_COUPLE {{{
-- delete from couples
-- if this is the last couple in a call, delete the call instead
-- return boolean
CREATE OR REPLACE FUNCTION callrec.DEL_COUPLE(callrec.couples.id%TYPE)
  RETURNS BOOLEAN
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _cplid ALIAS FOR $1;
      _done  BOOLEAN;
    BEGIN
      SELECT INTO _done callrec._DEL_EMPTY_CALL(_cplid);

      -- if the _DEL_EMPTY_CALL() above processed any row(s), then
      -- couples get deleted through a refint constraint;
      -- else, we will get rid of them here

      IF NOT _done THEN
        DELETE FROM callrec.couples
        WHERE id = _cplid;
      END IF;

      RETURN FOUND;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.DEL_COUPLE(callrec.couples.id%TYPE) TO GROUP callrecgrp;
-- }}}

