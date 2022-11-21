
 create or replace function callrec.cr_concat( text, text ) returns text as '
 begin
     if $1 isnull then
         return $2;
     else
        return $1 || $2;
     end if;
 end;' language 'plpgsql';

 GRANT EXECUTE ON FUNCTION callrec.cr_concat(text, text ) TO GROUP callrecgrp;

 create or replace function callrec.cr_concat_fin(text) returns text as '
 begin
     return $1;
 end;' language 'plpgsql';

 GRANT EXECUTE ON FUNCTION callrec.cr_concat_fin(text) TO GROUP callrecgrp;

 create  aggregate callrec.cr_concat (
     basetype = text,
     sfunc = callrec.cr_concat,
     stype = text,
     finalfunc = callrec.cr_concat_fin);

