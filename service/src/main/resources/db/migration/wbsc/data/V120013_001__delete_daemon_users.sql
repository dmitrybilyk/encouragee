delete from wbsc.user_role us using wbsc.sc_users u where u.userid = us.userid and u.daemon is true;
delete from wbsc.user_belongsto_ccgroup us using wbsc.sc_users u where u.userid = us.userid and u.daemon is true;
delete from wbsc.user_canevaluate_ccgroup us using wbsc.sc_users u where u.userid = us.userid and u.daemon is true;
delete from wbsc.user_questform us using wbsc.sc_users u where u.userid = us.userid and u.daemon is true;
delete from wbsc.user_right us using wbsc.sc_users u where u.userid = us.userid and u.daemon is true;
delete from wbsc.user_settings us using wbsc.sc_users u where u.userid = us.userid and u.daemon is true;
delete from wbsc.passwords us using wbsc.sc_users u where u.userid = us.userid and u.daemon is true;
DO $$
    DECLARE
        evaluationsCount integer;
    BEGIN
        select count(*) into evaluationsCount from wbsc.evaluations e INNER JOIN wbsc.sc_users u on e.evaluatorid = u.userid and u.daemon is true;
        if evaluationsCount = 0 then
            delete from wbsc.sc_users u where u.daemon is true;
         else
            RAISE EXCEPTION 'Daemon users have assigned evaluations';
        end if;
END
$$;
