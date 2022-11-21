INSERT INTO wbsc.appoptions (key, value, comment, company)
SELECT 'VISIBLE_AUDIT_REASONS_COUNT',
  '5',
  NULL,
  companyid
FROM wbsc.companies;