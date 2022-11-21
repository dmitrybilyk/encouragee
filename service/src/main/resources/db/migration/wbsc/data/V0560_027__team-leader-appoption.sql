INSERT INTO wbsc.appoptions
  (KEY, value, COMMENT, company)
VALUES
  ('IPCC_TEAM_LEADER_EQ',
  	'Team leader',
  	NULL,
  	(SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));