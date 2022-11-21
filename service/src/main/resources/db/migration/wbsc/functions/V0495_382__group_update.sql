CREATE OR REPLACE FUNCTION wbsc.ccgroups_update(IN _upd_ccgroupid integer, IN _upd_parentid integer) RETURNS boolean AS
$BODY$
DECLARE
	_upd_our wbsc.ccgroups%ROWTYPE;
	_upd_max_rightindex  integer;
	_new_par_rightindex integer;
BEGIN

	SELECT INTO  _upd_our * FROM wbsc.ccgroups WHERE ccgroupid = _upd_ccgroupid;


IF (_upd_parentid is null) THEN
	IF EXISTS (SELECT ccgroupid FROM wbsc.ccgroups WHERE parentid is null and company=_upd_our.company) THEN
		RAISE EXCEPTION 'cannot create two main agent groups';
	END IF;
ELSE
	IF NOT EXISTS (SELECT ccgroupid FROM wbsc.ccgroups WHERE ccgroupid = _upd_parentid and company=_upd_our.company) THEN
		RAISE EXCEPTION 'upd parentid group does not exists';
	ELSE
		IF EXISTS (SELECT ccgroupid FROM wbsc.ccgroups WHERE ccgroupid = _upd_parentid AND "leftindex" > _upd_our.leftindex AND "rightindex" < _upd_our.rightindex and company=_upd_our.company)THEN
			RAISE EXCEPTION 'new parentid cannot be in our subtree';
		END IF;
	END IF;
END IF;


_upd_max_rightindex := (SELECT "rightindex" FROM wbsc.ccgroups WHERE parentid is null and company=_upd_our.company);
--remove our subtree from tree
UPDATE wbsc.ccgroups SET "leftindex" = "leftindex" + _upd_max_rightindex - _upd_our.leftindex + 1,"rightindex" = "rightindex" + _upd_max_rightindex - _upd_our.leftindex + 1
WHERE "leftindex" >= _upd_our.leftindex AND "rightindex" <= _upd_our.rightindex and company=_upd_our.company;
--decrease numbers by removed tree
UPDATE wbsc.ccgroups SET "leftindex" = "leftindex" - _upd_our.rightindex + _upd_our.leftindex - 1 WHERE "leftindex" > _upd_our.leftindex and company=_upd_our.company;
UPDATE wbsc.ccgroups SET "rightindex" = "rightindex" - _upd_our.rightindex + _upd_our.leftindex - 1 WHERE "rightindex" > _upd_our.rightindex and company=_upd_our.company;

_new_par_rightindex := (SELECT "rightindex" FROM wbsc.ccgroups WHERE ccgroupid = _upd_parentid and company=_upd_our.company);
raise notice 'NEW rightindex %',_new_par_rightindex;
--insert tree on rightindex place
UPDATE wbsc.ccgroups SET "rightindex" = "rightindex" + _upd_our.rightindex - _upd_our.leftindex + 1 WHERE "rightindex" >= _new_par_rightindex and company=_upd_our.company;
UPDATE wbsc.ccgroups SET "leftindex" = "leftindex" + _upd_our.rightindex - _upd_our.leftindex + 1 WHERE "leftindex" > _new_par_rightindex and company=_upd_our.company;

UPDATE wbsc.ccgroups SET "leftindex" = "leftindex" - _upd_max_rightindex - 1 + _new_par_rightindex, "rightindex" = "rightindex" - _upd_max_rightindex - 1 + _new_par_rightindex
WHERE "leftindex" > _upd_max_rightindex and company=_upd_our.company;



UPDATE wbsc.ccgroups SET parentid = _upd_parentid WHERE ccgroupid=_upd_ccgroupid and company=_upd_our.company;

RETURN true;
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION wbsc.ccgroups_update(IN  integer, IN integer) OWNER TO postgres;

GRANT execute on function wbsc.ccgroups_update(_upd_ccgroupid integer, _upd_parentid integer) to wbscgrp;

