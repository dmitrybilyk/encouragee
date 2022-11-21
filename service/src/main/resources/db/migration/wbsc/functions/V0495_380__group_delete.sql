CREATE OR REPLACE FUNCTION wbsc.ccgroups_del() RETURNS trigger AS
$BODY$
DECLARE
	_del_row  wbsc.ccgroups%ROWTYPE;
	_del_child_cnt integer;
BEGIN


	SELECT INTO _del_row * FROM wbsc.ccgroups WHERE ccgroupid = OLD.ccgroupid;


IF (OLD.parentid is null) THEN
	_del_child_cnt = (SELECT COUNT(ccgroupid) FROM wbsc.ccgroups WHERE parentid = OLD.ccgroupid and company=_del_row.company);
	IF (_del_child_cnt > 1) THEN
		RAISE EXCEPTION 'Cannot delete main group which has more than 1 child';
	END IF;
END IF;

	UPDATE wbsc.ccgroups SET parentid = OLD.parentid WHERE parentid = OLD.ccgroupid and company=_del_row.company;
	--if inserted in the middle of the tree, then update only leftindexindex and rightindex -1
	UPDATE wbsc.ccgroups SET "leftindex" = "leftindex" - 1 WHERE "leftindex" > OLD.leftindex AND "leftindex"< OLD.rightindex and company=_del_row.company;
	UPDATE wbsc.ccgroups SET "rightindex" = "rightindex" - 1 WHERE "rightindex" > OLD.leftindex AND "rightindex" < OLD.rightindex and company=_del_row.company;
	--everything what is on the rightindex side of this rightindex, decrease by two
	UPDATE wbsc.ccgroups SET "rightindex" = "rightindex" - 2 WHERE "rightindex" > OLD.rightindex and company=_del_row.company;
	UPDATE wbsc.ccgroups SET "leftindex" = "leftindex" - 2 WHERE "leftindex" > OLD.rightindex and company=_del_row.company;

	RETURN OLD;
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION wbsc.ccgroups_del() OWNER TO postgres;


CREATE TRIGGER trig_del_ccgroups
  BEFORE DELETE
  ON wbsc.ccgroups
  FOR EACH ROW
  EXECUTE PROCEDURE wbsc.ccgroups_del();

GRANT execute on function wbsc.ccgroups_del() to wbscgrp;
