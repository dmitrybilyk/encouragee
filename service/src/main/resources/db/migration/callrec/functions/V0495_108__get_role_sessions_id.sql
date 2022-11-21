create or replace function callrec.get_role_sessions_id(p_role varchar)
returns varchar
as $$
begin
return 'role.' || p_role;
end;
$$ language plpgsql;
alter function callrec.get_role_sessions_id(varchar) owner to postgres;
grant execute on function callrec.get_role_sessions_id(varchar) to public;
grant execute on function callrec.get_role_sessions_id(varchar) to postgres;
grant execute on function callrec.get_role_sessions_id(varchar) to callrecgrp;
