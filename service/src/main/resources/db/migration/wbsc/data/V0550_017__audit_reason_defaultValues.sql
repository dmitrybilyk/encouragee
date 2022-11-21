INSERT INTO wbsc.auditReason (value, companyid) SELECT 'No customer contact', companyid FROM wbsc.companies;
INSERT INTO wbsc.auditReason (value, companyid) SELECT 'Playback Quality', companyid FROM wbsc.companies;
INSERT INTO wbsc.auditReason (value, companyid) SELECT 'Interaction Relevance', companyid FROM wbsc.companies;
INSERT INTO wbsc.auditReason (value, companyid) SELECT 'Interaction Unavailable', companyid FROM wbsc.companies;