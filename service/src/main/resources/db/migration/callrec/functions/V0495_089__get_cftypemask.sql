-- CREATE FUNCTION GET_CFTYPEMASK {{{
CREATE OR REPLACE FUNCTION callrec.GET_CFTYPEMASK(VARCHAR)
  RETURNS callrec.couples.cftypes%TYPE
  IMMUTABLE
  RETURNS NULL ON NULL INPUT
  LANGUAGE plpgsql
  AS '
    DECLARE
      _rv INTEGER;
      _type VARCHAR;
    BEGIN
      _type := UPPER($1);
      IF ''AUDIO'' = _type THEN
        _rv := 1;
      ELSIF ''MAG'' = _type THEN
        _rv := 2;
      ELSIF ''ARCHIVE'' = _type THEN
        _rv := 4;
      ELSIF ''IMAGE'' = _type THEN
        _rv := 8;
      ELSIF ''VIDEO'' = _type THEN
        _rv := 16;
      ELSIF ''RECD'' = _type THEN
        _rv := 32;
      ELSE
        RAISE EXCEPTION ''unknown value in GET_CFTYPEMASK(VARCHAR)'';
      END IF;
      RETURN CAST(_rv AS BIT(32));
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.GET_CFTYPEMASK(VARCHAR) TO GROUP callrecgrp;
-- }}}

