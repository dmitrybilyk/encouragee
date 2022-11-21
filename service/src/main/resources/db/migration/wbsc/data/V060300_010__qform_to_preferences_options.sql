INSERT INTO wbsc.appoptions
  (key, value, comment, company)
values
  ('AGENT_EXTENSION_LENGTH',
  (select value from wbsc.appoptions where key='DEFAULT_AGENT_EXTENSION_LENGTH'),
  NULL,
  (select companyid from wbsc.companies where display_name='DEFAULT'));

DELETE FROM wbsc.appoptions where key = 'DEFAULT_AGENT_EXTENSION_LENGTH';

