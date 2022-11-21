INSERT INTO wbsc.appoptions
  (key, value, COMMENT, company)
VALUES
  ('ENABLE_FORGOT_PASSWORD_BUTTON',
  	'true',
  	NULL,
  	(SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));