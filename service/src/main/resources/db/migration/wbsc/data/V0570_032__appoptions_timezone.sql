INSERT INTO wbsc.appoptions (key, value, comment, company)
SELECT 'TIMEZONE_FOR_SEARCH',
  'DEFAULT',
  NULL,
  companyid
FROM wbsc.companies
WHERE NOT EXISTS (
	SELECT key FROM wbsc.appoptions WhERE key = 'TIMEZONE_FOR_SEARCH'
);