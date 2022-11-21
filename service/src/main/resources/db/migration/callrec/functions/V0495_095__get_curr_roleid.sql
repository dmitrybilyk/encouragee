-- CREATE FUNCTION GET_CURR_ROLEID {{{
CREATE OR REPLACE FUNCTION callrec.GET_CURR_ROLEID()
  RETURNS callrec.roles.id%TYPE
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    BEGIN
      RETURN callrec.GET_CURR__ID(''seq_roles'');
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.GET_CURR_ROLEID() TO GROUP callrecgrp;
-- }}}

