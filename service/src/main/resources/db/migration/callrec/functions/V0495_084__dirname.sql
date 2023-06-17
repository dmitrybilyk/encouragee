-- CREATE FUNCTION DIRNAME {{{
CREATE OR REPLACE FUNCTION callrec.DIRNAME(VARCHAR)
  RETURNS VARCHAR
  IMMUTABLE
  RETURNS NULL ON NULL INPUT
  LANGUAGE plpgsql
  AS '
    DECLARE
      _path ALIAS FOR $1;
    BEGIN
      IF 0 = POSITION(''/'' IN _path) THEN
        RETURN '''';
      END IF;
      RETURN SUBSTRING(_path FROM ''^(.*)/'');
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.DIRNAME(VARCHAR) TO GROUP callrecgrp;
-- }}}
