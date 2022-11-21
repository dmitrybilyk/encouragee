ALTER TABLE wbsc.auditreason
  ADD CONSTRAINT auditreason_text_unique
    UNIQUE(value, companyid);
