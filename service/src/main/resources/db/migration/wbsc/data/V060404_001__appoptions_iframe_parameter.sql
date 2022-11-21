INSERT INTO wbsc.appoptions (key, value, comment, company)
SELECT 'IFRAME_PARAMETERS',
  'TokenOrigin=SCORECARD',
  NULL,
  companyid
FROM wbsc.companies
WHERE NOT EXISTS (
	SELECT key FROM wbsc.appoptions WhERE key = 'IFRAME_PARAMETERS'
);