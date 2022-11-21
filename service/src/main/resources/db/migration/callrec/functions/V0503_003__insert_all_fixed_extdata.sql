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

