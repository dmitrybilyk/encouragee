--CAL-7432
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
									          callrec.cfiles.stop_ts%TYPE)
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
      _cfid     callrec.cfiles.id%TYPE;
    BEGIN
      _cfid := callrec.ADD_CFILE(_cplid, _sgid, _bytes, _cktype, _ckvalue, _newtype, _newpath, _enc_key_id, _digest, _start_ts, _stop_ts);

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
