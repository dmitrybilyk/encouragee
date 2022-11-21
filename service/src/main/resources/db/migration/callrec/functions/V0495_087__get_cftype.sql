-- CREATE FUNCTION GET_CFTYPE {{{
-- provides filename extension -> cfiles.cftype%TYPE mapping.
-- $1 is a path or filename. returns NULL for values that don't
-- look like paths and unknown extensions.

CREATE OR REPLACE FUNCTION callrec.GET_CFTYPE(VARCHAR)
  RETURNS callrec.cfiles.cftype%TYPE
  IMMUTABLE
  RETURNS NULL ON NULL INPUT
  LANGUAGE plpgsql
  AS '
    DECLARE _ext VARCHAR;
            _path ALIAS FOR $1;
    BEGIN
      IF callrec.IS_LOCALABSPATH(_path) THEN
        _ext := LOWER(SUBSTRING(callrec.BASENAME(_path) FROM ''[^.]+$''));
        IF CHAR_LENGTH(_ext) = 0 THEN
          RETURN NULL;
        ELSIF _ext = ''mag'' THEN
          RETURN ''MAG'';
        ELSIF (_ext = ''mp3'' OR _ext = ''ogg'' OR _ext = ''wav'') THEN
          RETURN ''AUDIO'';
        ELSIF (_ext = ''zip'' OR _ext = ''tar'') THEN
          RETURN ''ARCHIVE'';
        ELSIF (_ext = ''avi'') THEN
          RETURN ''VIDEO'';
        ELSE
          -- other cftypes?
        END IF;
      END IF;
      -- catch ! callrec.IS_LOCALABSPATH(), the above ELSE,
      -- and anything else that might slip through
      RETURN NULL;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.GET_CFTYPE(VARCHAR) TO GROUP callrecgrp;
-- }}}

