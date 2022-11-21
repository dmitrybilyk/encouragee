-- CREATE FUNCTION add_couple_restore_request {{{
CREATE OR REPLACE FUNCTION callrec.add_couple_restore_request(callrec.restore_queue.cplid%TYPE)
RETURNS callrec.restore_queue.id%TYPE
AS $$
DECLARE
        p_cplid ALIAS FOR $1;
	_cplrow callrec.couples%ROWTYPE;
	_rqid INTEGER;
        _updated callrec.restore_queue.updated%TYPE;
BEGIN
_rqid := (SELECT id FROM callrec.restore_queue WHERE cplid = p_cplid);
IF _rqid IS NOT NULL  THEN
	RETURN _rqid;
END IF;
SELECT * INTO _cplrow FROM callrec.couples WHERE id = p_cplid;
IF _cplrow IS NULL THEN
	RAISE EXCEPTION 'couple id=% does not exist', p_cplid;
END IF;
IF NOT (_cplrow.deleted='D'and _cplrow.archived='A') OR _cplrow.b_location IS NULL THEN
	RAISE EXCEPTION 'couple id=% is not archived and deleted', p_cplid;
END IF;
_rqid := callrec.GET_NEXT_RESTOREQUEUEID();
_updated := now();
INSERT
	INTO callrec.restore_queue (id, callid, cplid, archive, updated)
	VALUES (_rqid, _cplrow.callid, p_cplid, _cplrow.b_location, _updated);
RETURN callrec.GET_CURR_RESTOREQUEUEID();
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION callrec.add_couple_restore_request(p_cplid INTEGER) TO GROUP callrecgrp;
-- }}}
