-- Dependencies: 1654
-- Data for Name: companies; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.companies ( display_name, configuration_equal_group, description) VALUES ( 'DEFAULT', NULL, NULL);


-- TOC entry 2102 (class 0 OID 17468)
-- Dependencies: 1611
-- Data for Name: ccgroups; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.ccgroups ( ccgroupname, description, parentid, leftindex, rightindex, company, external_id) VALUES ( 'Root group', 'Root description', NULL, 1, 2, (select companyid from wbsc.companies where display_name='DEFAULT'), NULL);

--
-- TOC entry 2100 (class 0 OID 17452)
-- Dependencies: 1609
-- Data for Name: database; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.database ( name, configuration_equal_group, dbtype, encryption_type, connection, connection_login, connection_password, company) VALUES ( 'ScoreCardDB', 'scorecard_business', 'LOCAL', 'PLAIN', NULL, NULL, NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.database ( name, configuration_equal_group, dbtype, encryption_type, connection, connection_login, connection_password, company) VALUES ( 'IPCC', 'scorecard-ipcc', 'IPCC', 'PLAIN', NULL, NULL, NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.database ( name, configuration_equal_group, dbtype, encryption_type, connection, connection_login, connection_password, company) VALUES ( 'GENESYS', 'wbsc-genesys', 'GENESYS', 'PLAIN', NULL, NULL, NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));


--
-- TOC entry 2125 (class 0 OID 18102)
-- Dependencies: 1655
-- Data for Name: languages; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES ( 'English (US)', 'en', 'US', true, 'American english'); 
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES ( 'Česky', 'cs', 'CZ', false, 'Čeština');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES ( 'Русский', 'ru', 'RU', false, 'русский язык');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES ( 'عربي', 'ar', 'EG', false, 'Arabic');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES ( 'Български', 'bg', 'BG', FALSE, 'Bulgarian'); 
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Deutsch', 'de', 'DE', false,'German'); 
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Polski', 'pl', 'PL', false,'Polish');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Română', 'ro', 'RO', false, 'Romanian'); 
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Slovensky', 'sk', 'SK', false,'Slovak'); 
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Slovenski', 'sl', 'SI', false,'Slovenian'); 
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Türkçe', 'tr', 'TR', false,'Turkish'); 
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'English', 'en', 'GB', false,'English');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Française', 'fr', 'FR', false,'French');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Español', 'es', 'ES', false,'Spanish');


--
-- TOC entry 2107 (class 0 OID 17516)
-- Dependencies: 1616
-- Data for Name: rights; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO  wbsc.rights ( name) VALUES ( 'VIEW_ALL_EVALS');
INSERT INTO  wbsc.rights ( name) VALUES ( 'VIEW_EVALS');
INSERT INTO  wbsc.rights ( name) VALUES ( 'SEND_EVAL_FEEDBACK');
INSERT INTO  wbsc.rights (name) VALUES ( 'VIEW_MY_EVALS');
INSERT INTO  wbsc.rights ( name) VALUES ( 'VIEW_TEAM_EVALS');
INSERT INTO  wbsc.rights ( name) VALUES ( 'CREATE_EVALS');
INSERT INTO  wbsc.rights ( name) VALUES ( 'REOPEN_EVALS');
INSERT INTO  wbsc.rights ( name) VALUES ( 'EVAL_AGENTS');
INSERT INTO  wbsc.rights ( name) VALUES ( 'EVAL_REMOVE_ALL');
INSERT INTO  wbsc.rights ( name) VALUES ( 'REPLACE_CALLS');
--INSERT INTO  wbsc.rights (  name) VALUES ( 'REQUEST_REPLACE');
INSERT INTO  wbsc.rights (  name) VALUES ( 'ADD_CALL');
INSERT INTO  wbsc.rights (  name) VALUES ( 'PLAN_ALL_EVALS');
--INSERT INTO  wbsc.rights (  name) VALUES ( 'EDIT_CLOSED_EVALS');
INSERT INTO  wbsc.rights (  name) VALUES ( 'VIEW_REPORTS');
INSERT INTO  wbsc.rights (  name) VALUES ( 'VIEW_ALL_REPORTS');
INSERT INTO  wbsc.rights (  name) VALUES ( 'MANAGE_ROLES');
INSERT INTO  wbsc.rights (  name) VALUES ( 'MANAGE_USERS');
INSERT INTO  wbsc.rights (  name) VALUES ( 'RESET_TEAM_PASSWORD');
INSERT INTO  wbsc.rights (  name) VALUES ( 'RESET_ALL_PASSWORD');
INSERT INTO  wbsc.rights (  name) VALUES ( 'EDIT_SETTINGS');
INSERT INTO  wbsc.rights (  name) VALUES ( 'EDIT_ADMIN_SETTING');
INSERT INTO  wbsc.rights (  name) VALUES ( 'MANAGE_QUESTIONNAIRES');
INSERT INTO  wbsc.rights (  name) VALUES ( 'VIEW_AUDIT');
INSERT INTO  wbsc.rights (  name) VALUES ( 'PLAN_GROUP_EVALS');
INSERT INTO  wbsc.rights (  name) VALUES ( 'EDIT_GLOBAL_TEMPLATES');
INSERT INTO  wbsc.rights (  name) VALUES ( 'EDIT_LICENSE');
INSERT INTO  wbsc.rights (  name) VALUES ( 'EDIT_INTERACTION_TYPES');
INSERT INTO  wbsc.rights (  name) VALUES ( 'MANAGE_ASSIGNED_EVALUATIONS');
INSERT INTO  wbsc.rights (  name) VALUES ( 'SELF_EVALUATE');


--
-- TOC entry 2106 (class 0 OID 17508)
-- Dependencies: 1615
-- Data for Name: roles; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.roles ( name, description, company) VALUES ( 'Supervisor', 'Plan, evaluate, view reports (and reset passwords) for own teams and sub-teams', (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.roles ( name, description, company) VALUES ( 'CC Manager', 'All non-administrative permissions (and reset passwords for own teams and sub-teams only)', (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.roles ( name, description, company) VALUES ( 'Team leader', 'Perform & manage evaluations and reports for own team', (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.roles ( name, description, company) VALUES ( 'Agent', 'View and send own evaluation feedback', (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.roles ( name, description, company) VALUES ( 'IT Administrator', 'Update license, edit settings, reset all passwords', (select companyid from wbsc.companies where display_name='DEFAULT'));


--
-- TOC entry 2108 (class 0 OID 17521)
-- Dependencies: 1617
-- Data for Name: role_right; Type: TABLE DATA; Schema: scorecard; Owner: -
--

--AGENT
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_EVALS'), (select roleid from  wbsc.roles where name='Agent'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='SEND_EVAL_FEEDBACK'), (select roleid from  wbsc.roles where name='Agent'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='SELF_EVALUATE'), (select roleid from  wbsc.roles where name='Agent'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REPLACE_CALLS'), (select roleid from  wbsc.roles where name='Agent'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='ADD_CALL'), (select roleid from  wbsc.roles where name='Agent'));

--TEAM LEADER
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_MY_EVALS'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_TEAM_EVALS'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='CREATE_EVALS'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EVAL_AGENTS'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REPLACE_CALLS'), (select roleid from  wbsc.roles where name='Team leader'));
--INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REQUEST_REPLACE'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='ADD_CALL'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_REPORTS'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='MANAGE_ASSIGNED_EVALUATIONS'), (select roleid from  wbsc.roles where name='Team leader'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='PLAN_GROUP_EVALS'), (select roleid from  wbsc.roles where name='Team leader'));

--SUPERVISOR
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_MY_EVALS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_TEAM_EVALS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_ALL_EVALS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='CREATE_EVALS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REOPEN_EVALS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EVAL_AGENTS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REPLACE_CALLS'), (select roleid from  wbsc.roles where name='Supervisor'));
--INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REQUEST_REPLACE'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='ADD_CALL'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='PLAN_GROUP_EVALS'), (select roleid from  wbsc.roles where name='Supervisor'));



--INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EDIT_CLOSED_EVALS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_REPORTS'), (select roleid from  wbsc.roles where name='Supervisor'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='RESET_TEAM_PASSWORD'), (select roleid from  wbsc.roles where name='Supervisor'));

--CC MANAGER
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_MY_EVALS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_TEAM_EVALS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_ALL_EVALS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='CREATE_EVALS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EVAL_AGENTS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EVAL_REMOVE_ALL'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REPLACE_CALLS'), (select roleid from  wbsc.roles where name='CC Manager'));
--INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='REQUEST_REPLACE'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='ADD_CALL'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='PLAN_GROUP_EVALS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='PLAN_ALL_EVALS'), (select roleid from  wbsc.roles where name='CC Manager'));


--INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EDIT_CLOSED_EVALS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_REPORTS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_ALL_REPORTS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='MANAGE_ROLES'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='MANAGE_USERS'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='RESET_TEAM_PASSWORD'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EDIT_ADMIN_SETTING'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='MANAGE_QUESTIONNAIRES'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='VIEW_AUDIT'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EDIT_GLOBAL_TEMPLATES'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EDIT_INTERACTION_TYPES'), (select roleid from  wbsc.roles where name='CC Manager'));

--ADMIN
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='RESET_ALL_PASSWORD'), (select roleid from  wbsc.roles where name='IT Administrator'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EDIT_SETTINGS'), (select roleid from  wbsc.roles where name='IT Administrator'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='EDIT_LICENSE'), (select roleid from  wbsc.roles where name='IT Administrator'));





--
-- TOC entry 2101 (class 0 OID 17457)
-- Dependencies: 1610
-- Data for Name: sc_users; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.sc_users (name, surname, login, database, sync, status, phone, agentid, identificator_used, delegator, delegation_from, delegation_to, send_feedback, email, language, company, external_id, deactivation_date)
 VALUES ( 'Admin', 'Admin', 'admin', (select databaseid from wbsc.database where name='ScoreCardDB'), false, 'ACTIVE', '12345', '123', 'PHONE', NULL, NULL, NULL, NULL, 'john.doe@company.com', 
 (select languageid from wbsc.languages where display_name='English') , (select companyid from wbsc.companies where display_name='DEFAULT'), NULL, NULL);

INSERT INTO wbsc.passwords (userid, "password", creation_date) VALUES ( currval('wbsc.seq_users'), '21232f297a57a5a743894a0e4a801fc3', '2001-01-01 00:00:00' );

INSERT INTO wbsc.sc_users (name, surname, login, database, sync, status, phone, agentid, identificator_used, delegator, delegation_from, delegation_to, send_feedback, email, language, company, external_id, deactivation_date)
 VALUES ( 'CcManager', 'CcManager', 'ccmanager', (select databaseid from wbsc.database where name='ScoreCardDB'), true, 'ACTIVE', '12345', '123', 'PHONE', NULL, NULL, NULL, NULL, 'jane.doe@company.com', 1, 1, NULL, NULL);

INSERT INTO wbsc.passwords (userid, "password", creation_date) VALUES ( currval('wbsc.seq_users'), '21232f297a57a5a743894a0e4a801fc3' , '2001-01-01 00:00:00' );

INSERT INTO wbsc.sc_users (name, surname, login, database, sync, status, phone, agentid, identificator_used, delegator, delegation_from, delegation_to, send_feedback, email, language, company, external_id, deactivation_date)
 VALUES ( 'ipccimporterdaemon', 'ipccimporterdaemon', 'ipccimporterdaemon', (select databaseid from wbsc.database where name='ScoreCardDB'), false, 'ACTIVE', '12345', '123', 'PHONE', NULL, NULL, NULL, NULL, 'jane.doe@company.com', 
 (select languageid from wbsc.languages where display_name='English'), (select companyid from wbsc.companies where display_name='DEFAULT'), NULL, NULL);

INSERT INTO wbsc.passwords (userid, "password", creation_date) VALUES ( currval('wbsc.seq_users'), '21232f297a57a5a743894a0e4a801fc3' , current_timestamp );

--
-- TOC entry 2105 (class 0 OID 17505)
-- Dependencies: 1614
-- Data for Name: user_belongsto_ccgroup; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.user_belongsto_ccgroup (userid, ccgroupid) VALUES ((select userid from wbsc.sc_users where surname='CcManager' and login='ccmanager'), (select ccgroupid from wbsc.ccgroups where parentid is null));
INSERT INTO wbsc.user_belongsto_ccgroup (userid, ccgroupid) VALUES ((select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon'), (select ccgroupid from wbsc.ccgroups where parentid is null));
INSERT INTO wbsc.user_belongsto_ccgroup (userid, ccgroupid) VALUES ((select userid from wbsc.sc_users where surname='Admin' and login='admin'), (select ccgroupid from wbsc.ccgroups where parentid is null));


--
-- TOC entry 2104 (class 0 OID 17502)
-- Dependencies: 1613
-- Data for Name: user_canevaluate_ccgroup; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.user_canevaluate_ccgroup (userid, ccgroupid) VALUES ((select userid from wbsc.sc_users where surname='CcManager' and login='ccmanager'), (select ccgroupid from wbsc.ccgroups where parentid is null));



--
-- TOC entry 2109 (class 0 OID 17524)
-- Dependencies: 1618
-- Data for Name: user_role; Type: TABLE DATA; Schema: scorecard; Owner: -
--

INSERT INTO wbsc.user_role (userid, roleid) VALUES ((select userid from wbsc.sc_users where surname='Admin' and login='admin'),( select roleid from  wbsc.roles where name='IT Administrator'));
INSERT INTO wbsc.user_role (userid, roleid) VALUES ((select userid from wbsc.sc_users where surname='CcManager' and login='ccmanager'),( select roleid from  wbsc.roles where name='CC Manager'));

--user right
insert into wbsc.user_right values((select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon'),(select rightid from  wbsc.rights where name='MANAGE_ROLES'));
insert into wbsc.user_right values((select userid from wbsc.sc_users where surname='ipccimporterdaemon' and login='ipccimporterdaemon'),(select rightid from  wbsc.rights where name='MANAGE_USERS'));


--application options
INSERT INTO wbsc.appoptions ( key, value, comment, company) VALUES ( 'IPCC_AGENT_EQ', 'Agent', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions ( key, value, comment, company) VALUES ( 'IPCC_SUPERVISOR_EQ', 'Supervisor', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'AGENT_EVALUATION_NOTIFICATION', 'true', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'NOT_APPLICABLE_ANSWER_CALCULATING_METHOD', 'METHOD1', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'RECORDS_ON_PAGE', '20', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'DASHBOARD_WIDGET_LIMIT', '20', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'PREFER_USER_LANG_FOR_SORTING', 'false', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));

-- Fill interaction_types with default data (default data for every company)  
INSERT INTO wbsc.interaction_types (name, type, company) select 'Call', 'SYSTEM', companyid from wbsc.companies;
INSERT INTO wbsc.interaction_types (name, type, company) select 'Screen', 'SYSTEM', companyid from wbsc.companies;
INSERT INTO wbsc.interaction_types (name, type, company) select 'CallScreen', 'SYSTEM', companyid from wbsc.companies;
INSERT INTO wbsc.interaction_types (name, type, company) select 'Email', 'USER', companyid from wbsc.companies;
INSERT INTO wbsc.interaction_types (name, type, company) select 'Chat', 'USER', companyid from wbsc.companies;
INSERT INTO wbsc.interaction_types (name, type, company) select 'Registration', 'USER', companyid from wbsc.companies;
INSERT INTO wbsc.interaction_types (name, type, company) select 'Other', 'USER', companyid from wbsc.companies;
