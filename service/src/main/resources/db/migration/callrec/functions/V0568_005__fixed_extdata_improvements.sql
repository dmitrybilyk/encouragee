DROP FUNCTION callrec.insertall_fixed_extdata( TEXT );

CREATE OR REPLACE FUNCTION callrec.insert_fixed_extdata(_id_col INTEGER, _val TEXT, _cplid INTEGER)
  RETURNS BOOLEAN AS
$BODY$
DECLARE
  _value        TEXT;
  _updated_rows INTEGER DEFAULT 0;

BEGIN
  _value:=coalesce(replace(replace(_val, E'\\', E'\\\\'), '''', ''''''), '');
  EXECUTE 'update callrec.couple_fixed_extdata set col_' || _id_col || '=''' || _value || ''' where cplid=' || _cplid ||
          ' and col_' || _id_col || ' IS DISTINCT FROM ''' || _value || '''';

  GET DIAGNOSTICS _updated_rows = ROW_COUNT;

  IF _updated_rows = 0
  THEN -- maybe there was already same record during update
    IF NOT EXISTS(SELECT 1
                  FROM callrec.couple_fixed_extdata
                  WHERE cplid = _cplid)
    THEN
      EXECUTE
      'insert into callrec.couple_fixed_extdata (cplid,col_' || _id_col || ') values(' || _cplid || ',''' || _value ||''')';
    END IF;
  END IF;

  RETURN TRUE;
END;
$BODY$
LANGUAGE 'plpgsql';

ALTER FUNCTION callrec.insert_fixed_extdata(_id_col INTEGER, _val TEXT, _cplid INTEGER ) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.insert_fixed_extdata(_id_col INTEGER, _val TEXT, _cplid INTEGER) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.insert_fixed_extdata(_id_col INTEGER, _val TEXT, _cplid INTEGER) TO callrecgrp;


CREATE OR REPLACE FUNCTION callrec.fixed_extdata_by_id_range(_key TEXT, _from_id INTEGER, _to_id INTEGER)
  RETURNS BOOLEAN AS
$BODY$
DECLARE
  curInner   RECORD;
  oneRow     RECORD;
  _id_col    INTEGER;
  _max_cplid INTEGER;
BEGIN

  SELECT INTO _id_col columnid
  FROM callrec.extdata_map
  WHERE "key" = _key;

  IF _id_col IS NULL
  THEN
    SELECT INTO _id_col columnid
    FROM callrec.extdata_map
    WHERE "key" IS NULL
    LIMIT 1;

    IF _id_col IS NULL
    THEN
      RAISE EXCEPTION 'ALL COLUMNS ARE FULL!';
    END IF;

    UPDATE callrec.extdata_map
    SET "key" = _key, ready = FALSE
    WHERE columnid = _id_col;

    RAISE NOTICE 'New column added for key "%": %', _key, _id_col;
  END IF;

  SELECT INTO _max_cplid max(id) FROM callrec.couples;
  IF (_max_cplid < _from_id)
  THEN
    RAISE NOTICE 'All couples processed.';
    RETURN FALSE;
  END IF;

  RAISE NOTICE 'Processing couples from id "%" for key "%" into column "%" started at %.', _from_id, _key, _id_col, clock_timestamp();

  FOR curInner IN SELECT   cplid,  value  FROM callrec.couple_extdata WHERE "key" = _key AND  cplid >= _from_id AND cplid < _to_id LOOP
    PERFORM callrec.insert_fixed_extdata(_id_col, curInner."value", curInner.cplid);
  END LOOP;

  RAISE NOTICE 'Processing couples from "%" for key "%" into column "%" finished at %.', _from_id, _key, _id_col, clock_timestamp();

  RETURN TRUE;
END;
$BODY$
LANGUAGE 'plpgsql';

ALTER FUNCTION callrec.fixed_extdata_by_id_range( TEXT, INTEGER, INTEGER ) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.fixed_extdata_by_id_range(TEXT, INTEGER, INTEGER) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.fixed_extdata_by_id_range(TEXT, INTEGER, INTEGER) TO callrecgrp;


CREATE OR REPLACE FUNCTION empty_fixed_extdata(_key TEXT)
  RETURNS BOOLEAN AS
$BODY$
DECLARE
  _id_col INTEGER;
BEGIN
  SELECT INTO _id_col columnid
  FROM callrec.extdata_map
  WHERE "key" = _key;
  IF _id_col IS NULL
  THEN
    RAISE EXCEPTION 'NO column with key=%', _key;
  END IF;
  UPDATE callrec.extdata_map
  SET "key" = NULL, ready = TRUE
  WHERE columnid = _id_col;
  EXECUTE 'ALTER TABLE callrec.couple_fixed_extdata DROP COLUMN col_' || _id_col;
  EXECUTE 'ALTER TABLE callrec.couple_fixed_extdata ADD COLUMN col_' || _id_col || ' text';
  EXECUTE 'CREATE INDEX _couple_extdata_lower_cplid_col' || _id_col ||
          '_idx   ON callrec.couple_fixed_extdata   USING btree  (lower(col_' || _id_col ||
          ') text_pattern_ops, cplid) WHERE lower(col_' || _id_col || ') IS NOT NULL;';
  RETURN TRUE;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
ALTER FUNCTION callrec.empty_fixed_extdata( TEXT )
OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.empty_fixed_extdata(TEXT) TO PUBLIC;
GRANT EXECUTE ON FUNCTION callrec.empty_fixed_extdata(TEXT) TO postgres;


create or replace function callrec.insert_fixed_extdata(_key text, _val text, _cplid integer) returns boolean
as
$BODY$
declare
	_value text;
	_id_col integer;
begin
 select into _id_col columnid from callrec.extdata_map where "key"=_key;
 if _id_col is null then
	raise exception 'COLUMN NOT DEFINED';
 end if;

_value:=coalesce(replace(replace(_val,E'\\',E'\\\\'),'''',''''''),'');
--test existence first
if EXISTS(select 1 from callrec.couple_fixed_extdata where cplid=_cplid) then
  execute 'update callrec.couple_fixed_extdata set col_'||_id_col||'='''||_value||''' where cplid='||_cplid ||' and col_' || _id_col || ' IS DISTINCT FROM ''' || _value || '''';
else
  execute 'insert into callrec.couple_fixed_extdata (cplid,col_'||_id_col||') values('||_cplid||','''||_value||''')';
end if;
return true;
end;
$BODY$
  LANGUAGE 'plpgsql';


ALTER FUNCTION callrec.insert_fixed_extdata(_key text, _val text, _cplid integer) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.insert_fixed_extdata(_key text, _val text, _cplid integer) TO public;
GRANT EXECUTE ON FUNCTION callrec.insert_fixed_extdata(_key text, _val text, _cplid integer) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.insert_fixed_extdata(_key text, _val text, _cplid integer) TO callrecgrp;
