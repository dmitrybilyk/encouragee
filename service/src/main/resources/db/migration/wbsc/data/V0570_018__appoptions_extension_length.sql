INSERT INTO wbsc.appoptions (key, value, comment, company)
SELECT 'DEFAULT_AGENT_EXTENSION_LENGTH',
  '4',
  NULL,
  companyid
FROM wbsc.companies
WHERE NOT EXISTS (
	SELECT key FROM wbsc.appoptions WhERE key = 'DEFAULT_AGENT_EXTENSION_LENGTH'
);