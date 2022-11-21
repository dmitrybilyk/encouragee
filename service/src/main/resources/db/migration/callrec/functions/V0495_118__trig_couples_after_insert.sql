-- CREATE FUNCTION TRIG_COUPLES_AFTER_INSERT {{{
-- increment calls.realcplcnt when inserting a couple
CREATE OR REPLACE FUNCTION callrec.TRIG_COUPLES_AFTER_INSERT()
  RETURNS TRIGGER
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      call RECORD;
    BEGIN
      -- maintain "the real couple count" in calls {{{
      SELECT INTO call *
        FROM callrec.calls
        WHERE
         id = NEW.callid;

      IF call.start_ts IS NULL
        OR call.start_ts > NEW.start_ts
      THEN call.start_ts = NEW.start_ts;
      END IF;

      IF call.stop_ts IS NULL
        OR call.stop_ts < NEW.stop_ts
      THEN call.stop_ts = NEW.stop_ts;
      END IF;

      UPDATE callrec.calls SET
         realcplcnt = COALESCE(realcplcnt, 0) + 1,
         start_ts = call.start_ts,
         stop_ts = call.stop_ts,
         length = COALESCE(EXTRACT(EPOCH FROM call.stop_ts - call.start_ts)::INTEGER, 0)
       WHERE
         id = NEW.callid;
      RETURN NULL;
    END;
  ';
-- }}}

