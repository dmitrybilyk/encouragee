CREATE OR REPLACE FUNCTION callrec.on_before_filter_delete()
  RETURNS trigger AS
$BODY$
DECLARE
	_id callrec.view_restriction_parts.view_restrictionid%TYPE;
	_found_usage integer;
BEGIN

SELECT into _id view_restrictionid  from callrec.view_restriction_parts where filterid=OLD.id limit 1;

IF _id is not null THEN
	select into _found_usage count(view_restrictionid) from callrec.roles where view_restrictionid =_id;
	IF _found_usage =0 THEN
		select into _found_usage count(view_restrictionid) from callrec.users where view_restrictionid =_id;
	END IF;
	IF _found_usage >0 THEN
		RAISE EXCEPTION 'Cannot delete filter id=% because view_filter id=% uses it.', OLD.id,_id;

	END IF;
END IF;
DELETE from callrec.view_restrictions where id=_id;
RETURN OLD;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION callrec.on_before_filter_delete() OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.on_before_filter_delete() TO postgres;
GRANT EXECUTE ON FUNCTION callrec.on_before_filter_delete() TO public;


CREATE TRIGGER trig_on_before_filter_delete
  BEFORE DELETE
  ON callrec.filters
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.on_before_filter_delete();
