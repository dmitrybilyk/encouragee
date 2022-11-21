CREATE OR REPLACE FUNCTION callrec.trig_cfiles_after_update()
  RETURNS "trigger" AS
$BODY$
    DECLARE
      _empty BIT(32);
      _oldcplid INTEGER;
    BEGIN
      _empty := CAST(0 AS BIT(32));

	select cplid into _oldcplid
        FROM callrec.cfiles cf
        WHERE cf.cplid = OLD.cplid
        AND cf.cftype = OLD.cftype;

    IF OLD.cplid <> NEW.cplid THEN
	--Changed cplid - change cftypes - add to new couple
	UPDATE callrec.couples
        SET
          cftypes = COALESCE(cftypes, _empty) | callrec.GET_CFTYPEMASK(NEW.cftype),
          cfcnt = COALESCE(cfcnt, 0) + 1
        WHERE
          id = NEW.cplid;
	--remove from old couple


	IF _oldcplid is null THEN
		UPDATE callrec.couples
		SET
		  cftypes = cftypes & ~callrec.GET_CFTYPEMASK(OLD.cftype),
		  cfcnt = greatest(cfcnt - 1,0)
		WHERE callrec.couples.id = OLD.cplid;
	 ELSE
		UPDATE callrec.couples
		SET
		  cfcnt = greatest(cfcnt - 1,0)
		WHERE id = OLD.cplid;
	  END IF;

	ELSE IF OLD.cftype<>NEW.cftype THEN


		IF _oldcplid is  null THEN
			--there is no other cfile with same cftype, remove this cftype
			UPDATE callrec.couples
			SET
			  cftypes = cftypes & ~callrec.GET_CFTYPEMASK(OLD.cftype)| callrec.GET_CFTYPEMASK(NEW.cftype)
			WHERE callrec.couples.id = OLD.cplid;
		ELSE
			--just add new (and if it was already there, nothing happends)
			UPDATE callrec.couples
			SET
			  cftypes = cftypes | callrec.GET_CFTYPEMASK(NEW.cftype)
			WHERE callrec.couples.id = OLD.cplid;

		END IF;

	END IF;
     END IF;
     RETURN NEW;
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;
ALTER FUNCTION callrec.trig_cfiles_after_update() OWNER TO postgres;


CREATE TRIGGER trigupdate
  AFTER UPDATE
  ON callrec.cfiles
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.trig_cfiles_after_update();

