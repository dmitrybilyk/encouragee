-- CREATE FUNCTION ADD_CFILE {{{
-- insert into calls
-- return call id
CREATE OR REPLACE FUNCTION callrec.ADD_CFILE(callrec.couples.id%TYPE,
                                     callrec.cfiles.sgid%TYPE,
                                     callrec.cfiles.cfsize%TYPE,
                                     callrec.cfiles.cktype%TYPE,
                                     callrec.cfiles.ckvalue%TYPE,
                                     callrec.cfiles.cftype%TYPE,
                                     callrec.cfiles.cfpath%TYPE
                                    )
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
    BEGIN
      SELECT INTO _id callrec.GET_NEXT_CFILEID();
      INSERT INTO callrec.cfiles (
        id,
        sgid,
        cplid,
        cftype,
		cktype,
		ckvalue,
		cfsize,
		cfpath
      ) VALUES (
        _id,
        _sgid,
        _cplid,
		_cftype,
		_cktype,
		_ckvalue,
		_bytes,
		_path
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
                                    callrec.cfiles.cfpath%TYPE)
              TO GROUP callrecgrp;
-- }}}

CREATE OR REPLACE FUNCTION callrec.ADD_CFILE(callrec.couples.id%TYPE,
                                     callrec.cfiles.cfsize%TYPE,
                                     callrec.cfiles.cktype%TYPE,
                                     callrec.cfiles.ckvalue%TYPE,
                                     callrec.cfiles.cftype%TYPE,
                                     callrec.cfiles.cfpath%TYPE
                                    )
  RETURNS callrec.cfiles.id%TYPE
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _id           callrec.cfiles.id%TYPE;
      _sgid         callrec.cfiles.sgid%TYPE;
      _cplid        ALIAS FOR $1;
      _bytes        ALIAS FOR $2;
      _cktype       ALIAS FOR $3;
      _ckvalue      ALIAS FOR $4;
      _cftype       ALIAS FOR $5;
      _path         ALIAS FOR $6;
    BEGIN
      SELECT INTO _id callrec.GET_NEXT_CFILEID();
      SELECT INTO _sgid callrec.GET_NEXT_SGRPID();
      INSERT INTO callrec.cfiles (
        id,
        sgid,
        cplid,
        cftype,
		cktype,
		ckvalue,
		cfsize,
		cfpath
      ) VALUES (
        _id,
        _sgid,
        _cplid,
		_cftype,
		_cktype,
		_ckvalue,
		_bytes,
		_path
      );
      RETURN _id;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.ADD_CFILE(callrec.couples.id%TYPE,
                                    callrec.cfiles.cfsize%TYPE,
                                    callrec.cfiles.cktype%TYPE,
                                    callrec.cfiles.ckvalue%TYPE,
                                    callrec.cfiles.cftype%TYPE,
                                    callrec.cfiles.cfpath%TYPE)
              TO GROUP callrecgrp;
-- }}}

CREATE OR REPLACE FUNCTION callrec.ADD_CFILE(callrec.couples.id%TYPE,
                                     callrec.cfiles.sgid%TYPE,
                                     callrec.cfiles.cfsize%TYPE,
                                     callrec.cfiles.cktype%TYPE,
                                     callrec.cfiles.ckvalue%TYPE,
                                     callrec.cfiles.cftype%TYPE,
                                     callrec.cfiles.cfpath%TYPE,
									 callrec.cfiles.enc_key_id%TYPE,
									 callrec.cfiles.digest%TYPE)
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
		digest
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
		_digest
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
									callrec.cfiles.digest%TYPE)
              TO GROUP callrecgrp;
-- }}}

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
									 callrec.cfiles.stop_ts%TYPE)
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
	    stop_ts
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
	    _stop_ts
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
									callrec.cfiles.stop_ts%TYPE)
              TO GROUP callrecgrp;
-- }}}
