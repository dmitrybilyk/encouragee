-- CREATE FUNCTION TRIG_CFILES_AFTER_INSERT {{{
-- increment couples.cfcnt when inserting a cfile
-- add this cfile's cftype into couples.cftypes
CREATE OR REPLACE FUNCTION callrec.TRIG_CFILES_AFTER_INSERT()
  RETURNS TRIGGER
  VOLATILE
  SECURITY DEFINER
  LANGUAGE plpgsql
  AS '
    DECLARE
      _empty BIT(32);
    BEGIN
      _empty := CAST(0 AS BIT(32));

      UPDATE callrec.couples
        SET
          cftypes = COALESCE(cftypes, _empty) | callrec.GET_CFTYPEMASK(NEW.cftype),
          cfcnt = COALESCE(cfcnt, 0) + 1
        WHERE
          id = NEW.cplid;
      RETURN NULL;
    END;
  ';
-- }}}

