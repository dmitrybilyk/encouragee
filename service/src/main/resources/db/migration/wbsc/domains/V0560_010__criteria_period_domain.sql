ALTER DOMAIN wbsc.criteria_period_domain DROP CONSTRAINT answers_compliance_switch;
ALTER DOMAIN wbsc.criteria_period_domain ADD CONSTRAINT answers_compliance_switch CHECK (VALUE IN (
  'YESTERDAY', 'LAST_WEEK', 'THIS_WEEK', 'LAST_MONTH', 'THIS_MONTH', 'NEXT_MONTH', 'THIS_YEAR', 'LAST_YEAR',
  'FIRST_QUARTER', 'SECOND_QUARTER', 'THIRD_QUARTER', 'FOURTH_QUARTER', 'THIS_QUARTER', 'CUSTOM_LAST_WEEK',
  'CUSTOM_THIS_WEEK', 'CUSTOM_NEXT_WEEK', 'CUSTOM_LAST_MONTH', 'CUSTOM_THIS_MONTH', 'CUSTOM_NEXT_MONTH',
  'CUSTOM_LAST_YEAR', 'CUSTOM_THIS_YEAR', 'LAST_3_DAYS', 'LAST_2_WEEKS', 'LAST_6_MONTHS', 'LAST_QUARTER', 'LAST_30_DAYS'));