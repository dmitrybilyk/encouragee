-- CREATE FUNCTION ADD_CALL {{{
-- Insert into calls (call id plus the "observed" number of couples).
-- $1 is the number of couples CallREC has recorded (calls.num_couples
-- in the legacy schema). Triggers on couples maintain another number
-- in calls.realcplcnt.
--
-- Note: this interface abuses the fact that CallREC runs INSERTs after
-- the call has ended. ADD_CALL() could probably simply lose the parameter
-- when this problem is fixed, and calls.cplcnt will be maintained through
-- UPDATE statements issued at appropriate times.
--
-- Return id of the newly created call.

CREATE OR REPLACE FUNCTION callrec.ADD_CALL(callrec.calls.cplcnt%TYPE)
  RETURNS callrec.calls.id%TYPE
  VOLATILE
  CALLED ON NULL INPUT
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _id callrec.calls.id%TYPE;
      _cplcnt ALIAS FOR $1;
    BEGIN
      SELECT INTO _id callrec.GET_NEXT_CALLID();
      INSERT INTO callrec.calls (id, cplcnt) VALUES (_id, _cplcnt);
      RETURN _id;
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.ADD_CALL(callrec.calls.cplcnt%TYPE) TO GROUP callrecgrp;
-- }}}

