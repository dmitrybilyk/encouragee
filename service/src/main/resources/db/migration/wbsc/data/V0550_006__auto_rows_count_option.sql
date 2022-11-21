--application options
INSERT INTO wbsc.appoptions
  ( key, value, comment, company)
VALUES
  ( 'AUTO_RECORDS_ON_PAGE', 'true', NULL, (select companyid from wbsc.companies));