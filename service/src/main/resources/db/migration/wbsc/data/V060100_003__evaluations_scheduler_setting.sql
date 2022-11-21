INSERT INTO wbsc.appoptions
  (key, value, "comment", company)
VALUES
  ('EVALUATIONS_SCHEDULER_RUN',
   '03:00',
   NULL,
   (SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));