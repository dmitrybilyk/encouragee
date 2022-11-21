-- application options for the encourage iframe
INSERT INTO wbsc.appoptions
  (key, value, comment, company)
values
  ('ENCOURAGE_AUTHORIZATION_URL',
  'http://localhost:8101/api/authenticate/token/',
  NULL,
  (select companyid from wbsc.companies where display_name='DEFAULT'));

INSERT INTO wbsc.appoptions
  (key, value, comment, company)
values
  ('ENCOURAGE_API_URL',
  'http://localhost:8102/api/',
  NULL,
  (select companyid from wbsc.companies where display_name='DEFAULT'));
