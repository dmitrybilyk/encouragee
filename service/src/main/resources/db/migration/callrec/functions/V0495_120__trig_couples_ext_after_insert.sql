CREATE OR REPLACE FUNCTION callrec.trig_couples_ext_after_insert()
  RETURNS trigger AS
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
