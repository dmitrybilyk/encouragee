INSERT INTO wbsc.appoptions (key, value, comment, company)
values ( 'USER_FOR_SURVEY', 'ccmanager', NULL,
    (select companyid from wbsc.companies where display_name='DEFAULT'));