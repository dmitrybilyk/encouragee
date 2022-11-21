CREATE OR REPLACE FUNCTION callrec.GET_NEXT_SGRPID()
  RETURNS callrec.cfiles.sgid%TYPE
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    BEGIN
      RETURN callrec.GET_NEXT__ID(''seq_streamgroups'');
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.GET_NEXT_SGRPID() TO GROUP callrecgrp;

