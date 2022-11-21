CREATE OR REPLACE FUNCTION callrec.trig_couples_ext_after_insert()
  RETURNS "trigger" AS
$BODY$
    DECLARE
      	_id_col integer;
    BEGIN

       select into _id_col columnid from callrec.extdata_map where "key"=NEW.key;
 if _id_col is null then

 return NEW;
 else

 perform insert_fixed_extdata(NEW.key, NEW.value, NEW.cplid);
 end if;
return NEW;
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;
ALTER FUNCTION callrec.trig_couples_ext_after_insert() OWNER TO postgres;


CREATE TRIGGER trig_on_after_extdata_update
  AFTER UPDATE
  ON callrec.couple_extdata
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.trig_couples_ext_after_insert();

CREATE TRIGGER trig_on_after_extdata_insert
  AFTER INSERT
  ON callrec.couple_extdata
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.trig_couples_ext_after_insert();
