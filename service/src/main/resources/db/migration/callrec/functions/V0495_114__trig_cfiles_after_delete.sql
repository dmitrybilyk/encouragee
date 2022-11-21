-- CREATE FUNCTION TRIG_CFILES_AFTER_DELETE {{{
-- decrement couples.cfcnt when deleting a cfile
-- remove this cfile's cftype from couples.cftypes if it was
-- the last file of this type associated with the couple
CREATE OR REPLACE FUNCTION callrec.trig_cfiles_after_delete()
  RETURNS "trigger" AS
$BODY$
    DECLARE

    BEGIN
    PERFORM cplid
        FROM callrec.cfiles cf
        WHERE cf.cplid = OLD.cplid
        AND cf.cftype = OLD.cftype;

      IF NOT FOUND THEN
        UPDATE callrec.couples
        SET
          cftypes = cftypes & ~callrec.GET_CFTYPEMASK(OLD.cftype),
          cfcnt = greatest(cfcnt - 1,0)
        WHERE callrec.couples.id = OLD.cplid;
        ELSE
	UPDATE callrec.couples
        SET
          cfcnt = greatest(cfcnt - 1,0)
        WHERE
          id = OLD.cplid;

      END IF;

      RETURN NULL;
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;
ALTER FUNCTION callrec.trig_cfiles_after_delete() OWNER TO postgres;
