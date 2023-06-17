-- CREATE FUNCTION GET_CFTYPEID {{{
CREATE OR REPLACE FUNCTION callrec.GET_CFTYPEID(VARCHAR)
  RETURNS INTEGER
  IMMUTABLE
  RETURNS NULL ON NULL INPUT
  LANGUAGE plpgsql
  AS '
    BEGIN
      RETURN CAST(callrec.GET_CFTYPEMASK($1) AS INTEGER);
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.GET_CFTYPEID(VARCHAR) TO GROUP callrecgrp;
-- }}}
