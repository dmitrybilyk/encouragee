INSERT INTO wbsc.user_role (userid, roleid) SELECT (select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon'), (select roleid from wbsc.roles where name='CC Manager')
where exists (select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon') and exists (select roleid from wbsc.roles where name='CC Manager') and not exists (SELECT (select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon'), (select roleid from wbsc.roles where name='CC Manager')
where exists (select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon') and exists (select roleid from wbsc.roles where name='CC Manager'));

UPDATE wbsc.passwords SET creation_date = to_date('01-01-2099', 'DD-MM-YYYY') WHERE passwordid = (select passwordid from wbsc.passwords where userid = (select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon') order by creation_date desc limit 1) and creation_date is null;


