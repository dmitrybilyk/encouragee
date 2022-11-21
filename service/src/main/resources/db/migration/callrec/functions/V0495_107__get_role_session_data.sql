create or replace function callrec.get_role_session_data(p_role_name varchar)
returns refcursor
as $$
declare
	_cursor refcursor;
begin
open _cursor for
	(select * from user_sessions where id = get_role_sessions_id(p_role_name));
return _cursor;
end;
$$ language plpgsql;
alter function callrec.get_role_session_data(varchar) owner to postgres;
grant execute on function callrec.get_role_session_data(varchar) to public;
grant execute on function callrec.get_role_session_data(varchar) to postgres;
grant execute on function callrec.get_role_session_data(varchar) to callrecgrp;
