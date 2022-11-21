DROP FUNCTION IF EXISTS callrec.ADD_CFILE(callrec.couples.id%TYPE,
                                          callrec.cfiles.sgid%TYPE,
                                          callrec.cfiles.cfsize%TYPE,
                                          callrec.cfiles.cktype%TYPE,
                                          callrec.cfiles.ckvalue%TYPE,
                                          callrec.cfiles.cftype%TYPE,
                                          callrec.cfiles.cfpath%TYPE,
                                          callrec.cfiles.enc_key_id%TYPE,
                                          callrec.cfiles.digest%TYPE,
                                          callrec.cfiles.start_ts%TYPE,
                                          callrec.cfiles.stop_ts%TYPE);

CREATE OR REPLACE FUNCTION callrec.ADD_CFILE(callrec.couples.id%TYPE,
                                             callrec.cfiles.sgid%TYPE,
                                             callrec.cfiles.cfsize%TYPE,
                                             callrec.cfiles.cktype%TYPE,
                                             callrec.cfiles.ckvalue%TYPE,
                                             callrec.cfiles.cftype%TYPE,
                                             callrec.cfiles.cfpath%TYPE,
                                             callrec.cfiles.enc_key_id%TYPE,
                                             callrec.cfiles.digest%TYPE,
                                             callrec.cfiles.start_ts%TYPE,
                                             callrec.cfiles.stop_ts%TYPE,
                                             callrec.cfiles.archived%TYPE,
                                             callrec.cfiles.restored%TYPE)
  RETURNS callrec.cfiles.id%TYPE
VOLATILE
CALLED ON NULL INPUT
SECURITY DEFINER
LANGUAGE plpgsql
AS '
DECLARE
  _id           callrec.cfiles.id%TYPE;
  _cplid        ALIAS FOR $1;
  _sgid         ALIAS FOR $2;
  _bytes        ALIAS FOR $3;
  _cktype       ALIAS FOR $4;
  _ckvalue      ALIAS FOR $5;
  _cftype       ALIAS FOR $6;
  _path         ALIAS FOR $7;
  _enc_key_id   ALIAS FOR $8;
  _digest       ALIAS FOR $9;
  _start_ts     ALIAS FOR $10;
  _stop_ts      ALIAS FOR $11;
  _archived     ALIAS FOR $12;
  _restored     ALIAS FOR $13;
  _real_sgid    callrec.cfiles.sgid%TYPE;
BEGIN
  SELECT INTO _id callrec.GET_NEXT_CFILEID();
  if _sgid is not null then
    _real_sgid:=_sgid;
  else
    SELECT INTO _real_sgid callrec.GET_NEXT_SGRPID();
  end if;
  INSERT INTO callrec.cfiles (
    id,
    sgid,
    cplid,
    cftype,
    cktype,
    ckvalue,
    cfsize,
    cfpath,
    enc_key_id,
    digest,
    start_ts,
    stop_ts,
    archived,
    restored
  ) VALUES (
    _id,
    _real_sgid,
    _cplid,
    _cftype,
    _cktype,
    _ckvalue,
    _bytes,
    _path,
    _enc_key_id,
    _digest,
    _start_ts,
    _stop_ts,
    _archived,
    _restored
  );
  RETURN _id;
END;
';

GRANT EXECUTE ON FUNCTION callrec.ADD_CFILE(callrec.couples.id%TYPE,
                                            callrec.cfiles.sgid%TYPE,
                                            callrec.cfiles.cfsize%TYPE,
                                            callrec.cfiles.cktype%TYPE,
                                            callrec.cfiles.ckvalue%TYPE,
                                            callrec.cfiles.cftype%TYPE,
                                            callrec.cfiles.cfpath%TYPE,
                                            callrec.cfiles.enc_key_id%TYPE,
                                            callrec.cfiles.digest%TYPE,
                                            callrec.cfiles.start_ts%TYPE,
                                            callrec.cfiles.stop_ts%TYPE,
                                            callrec.cfiles.archived%TYPE,
                                            callrec.cfiles.restored%TYPE)
TO GROUP callrecgrp;


CREATE OR REPLACE FUNCTION callrec.DECODE_STREAMGROUP(callrec.couples.id%TYPE,
                                              callrec.cfiles.sgid%TYPE,
                                              callrec.cfiles.cfsize%TYPE,
                                              callrec.cfiles.cktype%TYPE,
                                              callrec.cfiles.ckvalue%TYPE,
                                              callrec.cfiles.cftype%TYPE,
                                              callrec.cfiles.cfpath%TYPE,
                                              callrec.cfiles.enc_key_id%TYPE,
                                              callrec.cfiles.digest%TYPE,
                                              callrec.cfiles.start_ts%TYPE,
                                              callrec.cfiles.stop_ts%TYPE,
                                              callrec.cfiles.archived%TYPE,
                                              callrec.cfiles.restored%TYPE)
  RETURNS callrec.cfiles.id%TYPE
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _cplid    ALIAS FOR $1;
      _sgid     ALIAS FOR $2;
      _bytes    ALIAS FOR $3;
      _cktype   ALIAS FOR $4;
      _ckvalue  ALIAS FOR $5;
      _newtype  ALIAS FOR $6;
      _newpath  ALIAS FOR $7;
      _enc_key_id  ALIAS FOR $8;
	  _digest   ALIAS FOR $9;
	  _start_ts     ALIAS FOR $10;
	  _stop_ts      ALIAS FOR $11;
	  _archived     ALIAS FOR $12;
    _restored     ALIAS FOR $13;
      _cfid     callrec.cfiles.id%TYPE;
    BEGIN
      _cfid := callrec.ADD_CFILE(_cplid, _sgid, _bytes, _cktype, _ckvalue, _newtype, _newpath, _enc_key_id, _digest, _start_ts, _stop_ts, _archived, _restored);

      DELETE FROM callrec.decode_queue
        WHERE sgid = _sgid;

      DELETE FROM callrec.cfiles
        WHERE callrec.cfiles.sgid = _sgid
          AND callrec.cfiles.cplid = _cplid
          AND id != _cfid;

      IF NOT FOUND THEN
        RAISE EXCEPTION ''Nonexistent or empty streamgroup: %'', _sgid;
      END IF;

      RETURN _cfid;
    END;
  ';


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
                                     _startDates[_cnt], _stopDates[_cnt], NULL, NULL);
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


