
CREATE OR REPLACE FUNCTION callrec.on_before_role_delete()
  RETURNS trigger AS
$BODY$
DECLARE

BEGIN

IF OLD.view_restrictionid is not null THEN
		DELETE from callrec.view_restrictions where id=OLD.view_restrictionid;
END IF;

RETURN OLD;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;

ALTER FUNCTION callrec.on_before_role_delete() OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.on_before_role_delete() TO public;
GRANT EXECUTE ON FUNCTION callrec.on_before_role_delete() TO postgres;


CREATE TRIGGER trig_on_after_role_delete
  AFTER DELETE
  ON callrec.roles
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.on_before_role_delete();
