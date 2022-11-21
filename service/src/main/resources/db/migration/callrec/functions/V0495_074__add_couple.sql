
-- CREATE FUNCTION ADD_COUPLE {{{
-- insert into couples
-- return couple id
-- fails if given NULL callid
CREATE OR REPLACE FUNCTION callrec.ADD_COUPLE(callrec.couples.callid%TYPE,
                                      callrec.couples.start_ts%TYPE,
                                      callrec.couples.stop_ts%TYPE,
                                      callrec.couples.callingip%TYPE,
                                      callrec.couples.calledip%TYPE,
                                      callrec.couples.callingnr%TYPE,
                                      callrec.couples.originalcallednr%TYPE,
                                      callrec.couples.finalcallednr%TYPE,
                                      callrec.couples.callingpartyname%TYPE,
                                      callrec.couples.calledpartyname%TYPE,
                                      callrec.couples.cpltype%TYPE,
                                      callrec.couples.problemstatus%TYPE,
                                      callrec.couples.sid%TYPE
                                      )
  RETURNS callrec.couples.id%TYPE

  AS
$BODY$
    DECLARE
      -- argument aliases {{{
      _id                   callrec.couples.id%TYPE;
      _callid               ALIAS FOR  $1;
      _start_ts             ALIAS FOR  $2;
      _stop_ts              ALIAS FOR  $3;
      _callingip            ALIAS FOR  $4;
      _calledip             ALIAS FOR  $5;
      _callingnr            ALIAS FOR  $6;
      _originalcallednr     ALIAS FOR  $7;
      _finalcallednr        ALIAS FOR  $8;
      _callingpartyname     ALIAS FOR  $9;
      _calledpartyname      ALIAS FOR $10;
      _cpltype              ALIAS FOR $11;
      _problemstatus        ALIAS FOR $12;
      _sid                  ALIAS FOR $13;
      -- }}}
    BEGIN

      -- check that we are being plugged under a (hopefully valid) call {{{
      -- invalid call id will get caught by the INSERT trigger on couples
      IF _callid IS NULL THEN
        RAISE EXCEPTION 'supply a call id';
      END IF;
      -- }}}

      -- insert into couples... {{{
      SELECT INTO _id callrec.GET_NEXT_COUPLEID();
      INSERT INTO callrec.couples (
        id,
        callid,
        start_ts,
        stop_ts,
        created_ts,
        callingip,
        calledip,
        callingnr,
        originalcallednr,
        finalcallednr,
        callingpartyname,
        calledpartyname,
        cpltype,
        problemstatus,
        sid,
        description,
        b_method,
        b_location,
        length
      ) VALUES (
        _id,
        _callid,
        _start_ts,
        _stop_ts,
        NOW(),
        _callingip,
        _calledip,
        _callingnr,
        _originalcallednr,
        _finalcallednr,
        _callingpartyname,
        _calledpartyname,
        _cpltype,
        _problemstatus,
        _sid,
        NULL,
        NULL,
        NULL,
        COALESCE(date_part('epoch'::text, _stop_ts - _start_ts)::integer, 0)
      );
      -- }}}

      RETURN _id;
    END;
    $BODY$
LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;

GRANT EXECUTE ON FUNCTION callrec.ADD_COUPLE(callrec.couples.callid%TYPE,
                                     callrec.couples.start_ts%TYPE,
                                     callrec.couples.stop_ts%TYPE,
                                     callrec.couples.callingip%TYPE,
                                     callrec.couples.calledip%TYPE,
                                     callrec.couples.callingnr%TYPE,
                                     callrec.couples.originalcallednr%TYPE,
                                     callrec.couples.finalcallednr%TYPE,
                                     callrec.couples.callingpartyname%TYPE,
                                     callrec.couples.calledpartyname%TYPE,
                                     callrec.couples.cpltype%TYPE,
                                     callrec.couples.problemstatus%TYPE,
                                     callrec.couples.sid%TYPE
                                    ) TO GROUP  callrecgrp;
-- }}}

--for migration!
CREATE OR REPLACE FUNCTION callrec.add_couple(callrec.couples.callid%TYPE,
                                      callrec.couples.start_ts%TYPE,
                                      callrec.couples.stop_ts%TYPE,
                                      callrec.couples.callingip%TYPE,
                                      callrec.couples.calledip%TYPE,
                                      callrec.couples.callingnr%TYPE,
                                      callrec.couples.originalcallednr%TYPE,
                                      callrec.couples.finalcallednr%TYPE,
                                      callrec.couples.callingpartyname%TYPE,
                                      callrec.couples.calledpartyname%TYPE,
                                      callrec.couples.cpltype%TYPE,
                                      callrec.couples.problemstatus%TYPE,
                                      callrec.couples.sid%TYPE,
                                      callrec.couples.description%TYPE,
                                      callrec.couples.cfcnt%TYPE,
				      callrec.couples.protected%TYPE



)
  RETURNS integer AS
$BODY$
    DECLARE
      -- argument aliases {{{
      _id                   callrec.couples.id%TYPE;
      _callid               ALIAS FOR  $1;
      _start_ts            ALIAS FOR  $2;
      _stop_ts             ALIAS FOR  $3;
      _callingip            ALIAS FOR  $4;
      _calledip             ALIAS FOR  $5;
      _callingnr            ALIAS FOR  $6;
      _originalcallednr     ALIAS FOR  $7;
      _finalcallednr        ALIAS FOR  $8;
      _callingpartyname     ALIAS FOR  $9;
      _calledpartyname      ALIAS FOR $10;
      _cpltype              ALIAS FOR $11;
      _problemstatus        ALIAS FOR $12;
      _sid                  ALIAS FOR $13;
      _description	    ALIAS for $14;
      _cfcnt 		    ALIAS for $15;
      _protected	    ALIAS for $16;

      -- }}}
    BEGIN

      -- check that we are being plugged under a (hopefully valid) call {{{
      -- invalid call id will get caught by the INSERT trigger on couples
      IF _callid IS NULL THEN
        RAISE EXCEPTION 'supply a call id';
      END IF;
      -- }}}

      -- insert into couples... {{{
      SELECT INTO _id callrec.GET_NEXT_COUPLEID();
      INSERT INTO callrec.couples (
        id,
        callid,
        start_ts,
        stop_ts,
        created_ts,
        callingip,
        calledip,
        callingnr,
        originalcallednr,
        finalcallednr,
        callingpartyname,
        calledpartyname,
        cpltype,
        problemstatus,
        sid,
        description,
        b_method,
        b_location,
	    cfcnt,
	    protected,
        length
      ) VALUES (
        _id,
        _callid,
        _start_ts,
        _stop_ts,
        NOW(),
        _callingip,
        _calledip,
        _callingnr,
        _originalcallednr,
        _finalcallednr,
        _callingpartyname,
        _calledpartyname,
        _cpltype,
        _problemstatus,
        _sid,
        _description,
        NULL,
        NULL,
	    _cfcnt,
	    _protected,
        COALESCE(date_part('epoch'::text, _stop_ts - _start_ts)::integer, 0)
      );
      -- }}}

      RETURN _id;
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;

GRANT EXECUTE ON FUNCTION callrec.ADD_COUPLE(callrec.couples.callid%TYPE,
                                      callrec.couples.start_ts%TYPE,
                                      callrec.couples.stop_ts%TYPE,
                                      callrec.couples.callingip%TYPE,
                                      callrec.couples.calledip%TYPE,
                                      callrec.couples.callingnr%TYPE,
                                      callrec.couples.originalcallednr%TYPE,
                                      callrec.couples.finalcallednr%TYPE,
                                      callrec.couples.callingpartyname%TYPE,
                                      callrec.couples.calledpartyname%TYPE,
                                      callrec.couples.cpltype%TYPE,
                                      callrec.couples.problemstatus%TYPE,
                                      callrec.couples.sid%TYPE,
                                      callrec.couples.description%TYPE,
                                      callrec.couples.cfcnt%TYPE,
    				      callrec.couples.protected%TYPE


                                    ) TO GROUP  callrecgrp;


CREATE OR REPLACE FUNCTION callrec.add_couple(callrec.couples.callid%TYPE,
                                      callrec.couples.start_ts%TYPE,
                                      callrec.couples.stop_ts%TYPE,
                                      callrec.couples.callingip%TYPE,
                                      callrec.couples.calledip%TYPE,
                                      callrec.couples.callingnr%TYPE,
                                      callrec.couples.originalcallednr%TYPE,
                                      callrec.couples.finalcallednr%TYPE,
                                      callrec.couples.callingpartyname%TYPE,
                                      callrec.couples.calledpartyname%TYPE,
                                      callrec.couples.cpltype%TYPE,
                                      callrec.couples.problemstatus%TYPE,
                                      callrec.couples.sid%TYPE,
                                      callrec.couples.description%TYPE,
                                      callrec.couples.protected%TYPE,
                                      callrec.couples.b_method%TYPE,
                                      callrec.couples.b_location%TYPE,
                                      callrec.couples.created_ts%TYPE,
                                      callrec.couples.synchronized%TYPE,
                                      callrec.couples.mixed%TYPE,
                                      callrec.couples.deleted%TYPE,
                                      callrec.couples.restored%TYPE,
                                      callrec.couples.archived%TYPE,
                                      callrec.couples.evaluated%TYPE,
                                      callrec.couples.state%TYPE)

  RETURNS integer AS
$BODY$
    DECLARE
      -- argument aliases {{{
      _id                   callrec.couples.id%TYPE;
      _callid               ALIAS FOR  $1;
      _start_ts            ALIAS FOR  $2;
      _stop_ts             ALIAS FOR  $3;
      _callingip            ALIAS FOR  $4;
      _calledip             ALIAS FOR  $5;
      _callingnr            ALIAS FOR  $6;
      _originalcallednr     ALIAS FOR  $7;
      _finalcallednr        ALIAS FOR  $8;
      _callingpartyname     ALIAS FOR  $9;
      _calledpartyname      ALIAS FOR $10;
      _cpltype              ALIAS FOR $11;
      _problemstatus        ALIAS FOR $12;
      _sid                  ALIAS FOR $13;
      _description	    ALIAS for $14;
      _protected	    ALIAS for $15;
       _b_method		ALIAS for $16;
        _b_location		ALIAS for $17;
	_p_created_ts		ALIAS for $18;
	_synchronized		ALIAS for $19;
	_mixed			ALIAS for $20;
	_deleted		ALIAS for $21;
	_restored		ALIAS for $22;
	_archived		ALIAS for $23;
	_evaluated		ALIAS for $24;
	_state			ALIAS for $25;
	insert_created timestamptz;


      -- }}}
    BEGIN

      -- check that we are being plugged under a (hopefully valid) call {{{
      -- invalid call id will get caught by the INSERT trigger on couples
      IF _callid IS NULL THEN
        RAISE EXCEPTION 'supply a call id';
      END IF;
      -- }}}

if _p_created_ts is not null then
insert_created:=_p_created_ts;

	else
	insert_created:=NOW();
end if;

      -- insert into couples... {{{
      SELECT INTO _id callrec.GET_NEXT_COUPLEID();
      INSERT INTO callrec.couples (
        id,
        callid,
        start_ts,
        stop_ts,
        created_ts,
        callingip,
        calledip,
        callingnr,
        originalcallednr,
        finalcallednr,
        callingpartyname,
        calledpartyname,
        cpltype,
        problemstatus,
        sid,
        description,
        b_method,
        b_location,
	protected,
	synchronized,
	mixed,
	deleted,
	restored,
	archived,
	evaluated,
	state,
    length
      ) VALUES (
        _id,
        _callid,
        _start_ts,
        _stop_ts,
        insert_created,
        _callingip,
        _calledip,
        _callingnr,
        _originalcallednr,
        _finalcallednr,
        _callingpartyname,
        _calledpartyname,
        _cpltype,
        _problemstatus,
        _sid,
        _description,
        _b_method,
        _b_location,
        _protected,
        _synchronized,
        _mixed,
        _deleted,
        _restored,
        _archived,
        _evaluated,
        _state,
        COALESCE(date_part('epoch'::text, _stop_ts - _start_ts)::integer, 0)
      );
      -- }}}

      RETURN _id;
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;

ALTER FUNCTION callrec.add_couple(callrec.couples.callid%TYPE,
                                      callrec.couples.start_ts%TYPE,
                                      callrec.couples.stop_ts%TYPE,
                                      callrec.couples.callingip%TYPE,
                                      callrec.couples.calledip%TYPE,
                                      callrec.couples.callingnr%TYPE,
                                      callrec.couples.originalcallednr%TYPE,
                                      callrec.couples.finalcallednr%TYPE,
                                      callrec.couples.callingpartyname%TYPE,
                                      callrec.couples.calledpartyname%TYPE,
                                      callrec.couples.cpltype%TYPE,
                                      callrec.couples.problemstatus%TYPE,
                                      callrec.couples.sid%TYPE,
                                      callrec.couples.description%TYPE,
                                      callrec.couples.protected%TYPE,
                                      callrec.couples.b_method%TYPE,
                                      callrec.couples.b_location%TYPE,
                                      callrec.couples.created_ts%TYPE,
                                      callrec.couples.synchronized%TYPE,
                                      callrec.couples.mixed%TYPE,
                                      callrec.couples.deleted%TYPE,
                                      callrec.couples.restored%TYPE,
                                      callrec.couples.archived%TYPE,
                                      callrec.couples.evaluated%TYPE,
                                      callrec.couples.state%TYPE) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.add_couple(callrec.couples.callid%TYPE,
                                      callrec.couples.start_ts%TYPE,
                                      callrec.couples.stop_ts%TYPE,
                                      callrec.couples.callingip%TYPE,
                                      callrec.couples.calledip%TYPE,
                                      callrec.couples.callingnr%TYPE,
                                      callrec.couples.originalcallednr%TYPE,
                                      callrec.couples.finalcallednr%TYPE,
                                      callrec.couples.callingpartyname%TYPE,
                                      callrec.couples.calledpartyname%TYPE,
                                      callrec.couples.cpltype%TYPE,
                                      callrec.couples.problemstatus%TYPE,
                                      callrec.couples.sid%TYPE,
                                      callrec.couples.description%TYPE,
                                      callrec.couples.protected%TYPE,
                                      callrec.couples.b_method%TYPE,
                                      callrec.couples.b_location%TYPE,
                                      callrec.couples.created_ts%TYPE,
                                      callrec.couples.synchronized%TYPE,
                                      callrec.couples.mixed%TYPE,
                                      callrec.couples.deleted%TYPE,
                                      callrec.couples.restored%TYPE,
                                      callrec.couples.archived%TYPE,
                                      callrec.couples.evaluated%TYPE,
                                      callrec.couples.state%TYPE) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.add_couple(callrec.couples.callid%TYPE,
                                      callrec.couples.start_ts%TYPE,
                                      callrec.couples.stop_ts%TYPE,
                                      callrec.couples.callingip%TYPE,
                                      callrec.couples.calledip%TYPE,
                                      callrec.couples.callingnr%TYPE,
                                      callrec.couples.originalcallednr%TYPE,
                                      callrec.couples.finalcallednr%TYPE,
                                      callrec.couples.callingpartyname%TYPE,
                                      callrec.couples.calledpartyname%TYPE,
                                      callrec.couples.cpltype%TYPE,
                                      callrec.couples.problemstatus%TYPE,
                                      callrec.couples.sid%TYPE,
                                      callrec.couples.description%TYPE,
                                      callrec.couples.protected%TYPE,
                                      callrec.couples.b_method%TYPE,
                                      callrec.couples.b_location%TYPE,
                                      callrec.couples.created_ts%TYPE,
                                      callrec.couples.synchronized%TYPE,
                                      callrec.couples.mixed%TYPE,
                                      callrec.couples.deleted%TYPE,
                                      callrec.couples.restored%TYPE,
                                      callrec.couples.archived%TYPE,
                                      callrec.couples.evaluated%TYPE,
                                      callrec.couples.state%TYPE) TO  callrecgrp;
