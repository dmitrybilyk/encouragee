INSERT INTO wbsc.appoptions
  (key, value, COMMENT, company)
VALUES
  ('SHOW_INTERNAL_NOTES_JUST_TO_EVALUATOR',
  	'false',
  	NULL,
  	(SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));
