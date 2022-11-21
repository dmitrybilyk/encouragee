INSERT INTO wbsc.appoptions
  (key, value, COMMENT, company)
VALUES
  ('TO_CALCULATE_SUCCESS_RATE',
  	'false',
  	NULL,
  	(SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));
