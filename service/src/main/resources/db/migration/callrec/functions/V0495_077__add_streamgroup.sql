-- CREATE FUNCTION ADD_STREAMGROUP {{{
-- insert into _streamgroups, cfiles
-- return call id
CREATE OR REPLACE FUNCTION callrec.ADD_STREAMGROUP(callrec.couples.id%TYPE,
                                           INTEGER,
                                           INTEGER[],
                                           VARCHAR[],
                                           BIGINT[],
                                           VARCHAR[],
                                           VARCHAR[],
                                           TIMESTAMP WITH TIME ZONE[],
                                           TIMESTAMP WITH TIME ZONE[])
  RETURNS callrec.cfiles.id%TYPE
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _cplid        ALIAS FOR $1;
      _arrsz        ALIAS FOR $2;
      _bytes        ALIAS FOR $3;
      _cktypes      ALIAS FOR $4;
      _ckvalues     ALIAS FOR $5;
      _cftypes      ALIAS FOR $6;
      _paths        ALIAS FOR $7;
      _startDates   ALIAS FOR $8;
      _stopDates    ALIAS FOR $9;

      _cnt          INTEGER DEFAULT 1;
      _b            INTEGER;
      _sgid         callrec.cfiles.sgid%TYPE;
    BEGIN
      _sgid := callrec.GET_NEXT_SGRPID();

      WHILE _cnt <= _arrsz LOOP
        IF _bytes[_cnt] = -1 THEN
          _b := NULL;
        ELSE
          _b := _bytes[_cnt];
        END IF;
        PERFORM callrec.ADD_CFILE(_cplid, _sgid,
                                     _bytes[_cnt], _cktypes[_cnt],
                                     _ckvalues[_cnt], _cftypes[_cnt],
                                     _paths[_cnt], NULL, NULL,
                                     _startDates[_cnt], _stopDates[_cnt]);
        _cnt := _cnt + 1;
      END LOOP;
      RETURN _sgid;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.ADD_STREAMGROUP(callrec.couples.id%TYPE,
                                          INTEGER,
                                          INTEGER[],
                                          VARCHAR[],
                                          BIGINT[],
                                          VARCHAR[],
                                          VARCHAR[],
                                          TIMESTAMP WITH TIME ZONE[],
                                          TIMESTAMP WITH TIME ZONE[])
              TO GROUP callrecgrp;
-- }}}


