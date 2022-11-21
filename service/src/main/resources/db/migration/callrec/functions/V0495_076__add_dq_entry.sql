-- CREATE FUNCTION ADD_DQ_ENTRY {{{
-- insert into decoder_queue
-- return boolean (t ok, f failed)
CREATE OR REPLACE FUNCTION callrec.ADD_DQ_ENTRY(callrec.decode_queue.dqid%TYPE,
                                        callrec.cfiles.sgid%TYPE)
  RETURNS BOOLEAN
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _dqid  ALIAS FOR $1;
      _sgid  ALIAS FOR $2;
    BEGIN
      INSERT INTO callrec.decode_queue (
        dqid,
        sgid
      ) VALUES (
        _dqid,
        _sgid
      );
      RETURN FOUND;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.ADD_DQ_ENTRY(callrec.decode_queue.dqid%TYPE,
                                       callrec.cfiles.sgid%TYPE)
              TO GROUP callrecgrp;


