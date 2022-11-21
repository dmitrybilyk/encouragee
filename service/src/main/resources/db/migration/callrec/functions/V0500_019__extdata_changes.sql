
CREATE OR REPLACE FUNCTION callrec.empty_fixed_extdata(_key text)
  RETURNS boolean AS
$BODY$
declare
    _id_col integer;
begin
 select into _id_col columnid from callrec.extdata_map where "key"=_key;
 if _id_col is null then
 raise exception 'NO column with key=%',_key;
end if;
UPDATE callrec.extdata_map SET "key"=NULL where columnid=_id_col;
execute 'ALTER TABLE callrec.couple_fixed_extdata DROP COLUMN col_'||_id_col;
execute 'ALTER TABLE callrec.couple_fixed_extdata ADD COLUMN col_'||_id_col ||' text';
execute 'CREATE INDEX _couple_extdata_lower_col'||_id_col||'_idx   ON callrec.couple_fixed_extdata   USING btree  (lower(col_'||_id_col||') text_pattern_ops)';
return true;
end;
$BODY$
  LANGUAGE 'plpgsql';
ALTER FUNCTION callrec.empty_fixed_extdata(text) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.empty_fixed_extdata(text) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.empty_fixed_extdata(text) TO public;

update callrec.filter_parts set userval = 'ILIKE', sqlval='ILIKE', operator ='ILIKE' where formfield= 'sensitiveExtData' and (userval = 'LIKE' or sqlval='LIKE' or operator ='LIKE');

-- CAL-8348
CREATE OR REPLACE FUNCTION callrec.insertall_fixed_extdata(_key text)
  RETURNS boolean AS
$BODY$
declare
	curInner RECORD;
	oneRow RECORD;
begin
IF EXISTS (SELECT 1 from callrec.couple_extdata where "key"=_key) THEN
  FOR curInner IN SELECT * from callrec.couple_extdata where "key"=_key
      LOOP
  --RAISE NOTICE 'value = %  ',curInner.value;
   perform callrec.insert_fixed_extdata (_key,curInner."value",curInner.cplid);
   end LOOP;
  return true;
END IF;
return false;
end;
$BODY$
  LANGUAGE 'plpgsql';
ALTER FUNCTION callrec.insertall_fixed_extdata(text) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION callrec.insertall_fixed_extdata(text) TO postgres;
GRANT EXECUTE ON FUNCTION callrec.insertall_fixed_extdata(text) TO public;
