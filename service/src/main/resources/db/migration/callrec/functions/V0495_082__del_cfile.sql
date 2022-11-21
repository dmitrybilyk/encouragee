-- CREATE FUNCTION DEL_CFILE {{{
-- FIXME: broken! will delete a call with a single couple, but 2+ cfiles!
-- delete from cfiles
-- if this is the last cfile in a couple, delete the couple instead
-- return boolean
CREATE OR REPLACE FUNCTION callrec.DEL_CFILE(callrec.cfiles.cfpath%TYPE)
  RETURNS BOOLEAN
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _cfpath ALIAS FOR $1;
      _done   BOOLEAN;
    BEGIN

      SELECT INTO _done callrec._DEL_EMPTY_CALL(_cplid);

      -- if the last DELETE processed any row(s), then
      -- couples (and their cfiles) get deleted through a refint constraint
      -- else, we will get rid of them here

      IF NOT _done THEN
        SELECT INTO _done callrec._DEL_EMPTY_COUPLE(_cfpath);
      END IF;

      -- finally, this is not the last cfile of a last couple in a call,
      -- so we need to delete it this way

      IF NOT _done THEN
        DELETE FROM callrec.cfiles
        WHERE cfpath = _cfpath;
      END IF;

      RETURN FOUND;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.DEL_CFILE(callrec.cfiles.cfpath%TYPE) TO GROUP callrecgrp;
-- }}}

