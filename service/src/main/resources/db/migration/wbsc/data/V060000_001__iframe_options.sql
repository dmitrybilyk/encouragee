-- application options for the encourage iframe
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'CONVERSATION_SEARCH_PATH', '/conversations-public', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'IFRAME_MENU_TITLE', 'Conversations', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'IFRAME_TAB_TITLE', 'Conversations', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
