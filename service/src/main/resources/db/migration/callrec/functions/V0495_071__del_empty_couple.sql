-- CREATE FUNCTION _DEL_EMPTY_COUPLE {{{
-- delete a couple if a given cfile is its last
CREATE OR REPLACE FUNCTION callrec._DEL_EMPTY_COUPLE(callrec.cfiles.id%TYPE)
  RETURNS BOOLEAN
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _cfid   ALIAS FOR $1;
    BEGIN
      DELETE FROM callrec.couples
      WHERE callrec.cfiles.id = _cfid
        AND callrec.cfiles.cplid = id
        AND cfcnt = 1;
      RETURN FOUND;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec._DEL_EMPTY_COUPLE(callrec.cfiles.id%TYPE) TO GROUP callrecgrp;

CREATE OR REPLACE FUNCTION callrec._DEL_EMPTY_COUPLE(callrec.cfiles.cfpath%TYPE)
  RETURNS BOOLEAN
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _cfpath ALIAS FOR $1;
    BEGIN
      DELETE FROM callrec.couples
      WHERE callrec.cfiles.cfpath = _cfpath
        AND callrec.cfiles.cplid = id
        AND cfcnt = 1;
      RETURN FOUND;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec._DEL_EMPTY_COUPLE(callrec.cfiles.cfpath%TYPE) TO GROUP callrecgrp;
-- }}}

