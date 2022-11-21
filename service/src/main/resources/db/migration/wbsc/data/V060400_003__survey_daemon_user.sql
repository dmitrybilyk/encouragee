INSERT INTO wbsc.sc_users(
            name, surname, login,
            database, sync, status, phone,
            agentid, identificator_used, email,
            language,
            company, daemon)
    VALUES ('surveydaemon', 'surveydaemon', 'surveydaemon',
            (select databaseid from wbsc.database where name = 'ScoreCardDB'), false, 'ACTIVE', '12345',
            '12345', 'PHONE', 'surveydaemon@company.com',
            (select languageid from wbsc.languages where display_name = 'English'),
            (select companyid from wbsc.companies where display_name = 'DEFAULT'), true);

INSERT INTO wbsc.passwords (
            userid, "password", creation_date)
    VALUES ((select userid from wbsc.sc_users where login = 'surveydaemon'),
            '21232f297a57a5a743894a0e4a801fc3' , current_timestamp);

INSERT INTO wbsc.user_belongsto_ccgroup (
            userid, ccgroupid)
    VALUES ((select userid from wbsc.sc_users where login='surveydaemon'),
            (select ccgroupid from wbsc.ccgroups where parentid is null));

INSERT INTO wbsc.user_canevaluate_ccgroup (
            userid, ccgroupid)
    VALUES ((select userid from wbsc.sc_users where login='surveydaemon'),
            (select ccgroupid from wbsc.ccgroups where parentid is null));

INSERT INTO wbsc.user_role (
            userid, roleid)
    VALUES ((select userid from wbsc.sc_users where login='surveydaemon'),
            (select roleid from wbsc.roles where name='CC Manager'));

INSERT INTO wbsc.user_right (
            userid, rightid)
    VALUES ((select userid from wbsc.sc_users where login='surveydaemon'),
            (select rightid from wbsc.rights where name='EVAL_AGENTS'));

INSERT INTO wbsc.user_right (
            userid, rightid)
    VALUES ((select userid from wbsc.sc_users where login='surveydaemon'),
            (select rightid from wbsc.rights where name='PLAN_ALL_EVALS'));
