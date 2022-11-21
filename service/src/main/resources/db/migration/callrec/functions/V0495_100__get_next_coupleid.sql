-- CREATE FUNCTION GET_NEXT_COUPLEID {{{
CREATE OR REPLACE FUNCTION callrec.GET_NEXT_COUPLEID()
  RETURNS callrec.couples.id%TYPE
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    BEGIN
      RETURN callrec.GET_NEXT__ID(''seq_couples'');
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.GET_NEXT_COUPLEID() TO GROUP callrecgrp;
-- }}}

