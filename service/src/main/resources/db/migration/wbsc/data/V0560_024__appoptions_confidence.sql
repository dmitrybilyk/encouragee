INSERT INTO wbsc.appoptions (key, value, comment, company)
  values ( 'CONFIDENCE_MAYBE', '40', NULL,
    (select companyid from wbsc.companies where display_name='DEFAULT')
  );

INSERT INTO wbsc.appoptions (key, value, comment, company)
  values ( 'CONFIDENCE_PROBABLY', '50', NULL,
    (select companyid from wbsc.companies where display_name='DEFAULT')
  );

INSERT INTO wbsc.appoptions (key, value, comment, company)
  values ( 'CONFIDENCE_CERTAINLY', '65', NULL,
    (select companyid from wbsc.companies where display_name='DEFAULT')
  );
