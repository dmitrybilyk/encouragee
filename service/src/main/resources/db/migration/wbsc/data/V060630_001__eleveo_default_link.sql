INSERT INTO wbsc.appoptions (key, value, comment, company)
VALUES ( 'ELEVEO_WFM_URL', 'http://www.eleveo.com', NULL,
    (SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));
