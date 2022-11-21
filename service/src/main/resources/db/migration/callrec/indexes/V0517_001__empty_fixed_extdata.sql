CREATE OR REPLACE FUNCTION empty_fixed_extdata(_key text)
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
execute 'CREATE INDEX _couple_extdata_lower_cplid_col'||_id_col||'_idx   ON callrec.couple_fixed_extdata   USING btree  (lower(col_'||_id_col||') text_pattern_ops, cplid) WHERE lower(col_' || _id_col || ') IS NOT NULL;';
return true;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION empty_fixed_extdata(text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION empty_fixed_extdata(text) TO public;
GRANT EXECUTE ON FUNCTION empty_fixed_extdata(text) TO postgres;
