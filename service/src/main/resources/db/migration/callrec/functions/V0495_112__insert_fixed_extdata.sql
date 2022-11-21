create or replace function callrec.insert_fixed_extdata(_key text, _val text, _cplid integer) returns boolean
as
$BODY$
declare
	_value text;
	_id_col integer;

begin
 select into _id_col columnid from callrec.extdata_map where "key"=_key;
 if _id_col is null then
--insert this if is empty
SELECT into _id_col columnid FROM callrec.extdata_map WHERE "key" is null limit 1;
 if _id_col is null then
	raise exception 'ALL COLUMNS ARE FULL!';
 end if;
UPDATE callrec.extdata_map SET "key"=_key where columnid=_id_col;
raise notice 'inserting all';
perform callrec.insertall_fixed_extdata (_key);

end if;

_value:=coalesce(replace(replace(_val,E'\\',E'\\\\'),'''',''''''),'');
--insert
if EXISTS(select 1 from callrec.couple_fixed_extdata where cplid=_cplid) then
execute 'update callrec.couple_fixed_extdata set col_'||_id_col||'='''||_value||''' where cplid='||_cplid;
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
