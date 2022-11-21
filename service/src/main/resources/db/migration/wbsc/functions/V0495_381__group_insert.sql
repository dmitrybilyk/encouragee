CREATE OR REPLACE FUNCTION wbsc.ccgroups_insert()
  RETURNS trigger AS
$BODY$
DECLARE
	_new_par_rightindex integer;
BEGIN
IF (NEW.parentid is null) THEN
	IF EXISTS (SELECT ccgroupid FROM wbsc.ccgroups WHERE parentid is null and company=NEW.company) THEN
		RAISE EXCEPTION 'cannot insert two main agent groups';
	ELSE
	NEW.leftindex:=1;
	NEW.rightindex:=2;
		RETURN NEW;
	END IF;
ELSE
	IF NOT EXISTS (SELECT ccgroupid FROM wbsc.ccgroups WHERE ccgroupid = NEW.parentid and company=NEW.company)THEN
		RAISE EXCEPTION 'new parentid group does not exists';
	END IF;
END IF;

	_new_par_rightindex := (SELECT "rightindex" FROM wbsc.ccgroups WHERE ccgroupid = NEW.parentid and company=NEW.company);
	--shift rightindex all cos this one was inserted
	UPDATE wbsc.ccgroups SET "rightindex" = "rightindex" + 2 WHERE "rightindex" >= _new_par_rightindex and company=NEW.company;
	UPDATE wbsc.ccgroups SET "leftindex" = "leftindex" + 2 WHERE "leftindex" > _new_par_rightindex and company=NEW.company;

	NEW.leftindex:=_new_par_rightindex;
	NEW.rightindex:=_new_par_rightindex+1;
	return NEW;
    END;
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER;

ALTER FUNCTION wbsc.ccgroups_insert() OWNER TO postgres;



CREATE TRIGGER trig_ins_ccgroups
  BEFORE INSERT
  ON wbsc.ccgroups
  FOR EACH ROW
  EXECUTE PROCEDURE wbsc.ccgroups_insert();

GRANT execute on function wbsc.ccgroups_insert() to wbscgrp;
