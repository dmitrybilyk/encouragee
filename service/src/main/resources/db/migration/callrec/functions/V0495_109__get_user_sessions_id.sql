create or replace function callrec.get_user_sessions_id(p_login varchar)
returns varchar
as $$
begin
return 'user.' || p_login;
end;
$$ language plpgsql;
alter function callrec.get_user_sessions_id(varchar) owner to postgres;
grant execute on function callrec.get_user_sessions_id(varchar) to public;
grant execute on function callrec.get_user_sessions_id(varchar) to postgres;
grant execute on function callrec.get_user_sessions_id(varchar) to callrecgrp;
