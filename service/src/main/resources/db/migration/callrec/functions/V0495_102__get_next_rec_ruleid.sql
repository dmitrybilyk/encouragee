-- CREATE FUNCTION GET_NEXT_REC_RULEID {{{
CREATE OR REPLACE FUNCTION callrec.GET_NEXT_REC_RULEID()
  RETURNS callrec.rec_rules.id%TYPE
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    BEGIN
      RETURN callrec.GET_NEXT__ID(''seq_rec_rules'');
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.GET_NEXT_REC_RULEID() TO GROUP callrecgrp;
-- }}}
