INSERT INTO wbsc.appoptions
  (key, value, company)
SELECT
  'HIDDEN_NAVIGATION_ITEMS',
  'NavigationPanel_legacyCompareAgentsNode,'
  || 'NavigationPanel_legacyCompareAgentsNode,'
  || 'NavigationPanel_legacyCompareAgentsNode,'
  || 'NavigationPanel_legacyCompareEvaluatorsNode,'
  || 'NavigationPanel_legacyLeagueTableNode,'
  || 'NavigationPanel_legacyReportsNode,'
  || 'NavigationPanel_legacySkillOfAgentNode',
  companyid
FROM wbsc.companies;