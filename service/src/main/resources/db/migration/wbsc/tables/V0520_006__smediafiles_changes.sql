-- SC-5363
-- questions
ALTER TABLE wbsc.questions
  ADD COLUMN smediafileid integer;

ALTER TABLE wbsc.questions
  ADD CONSTRAINT questions_smediafileid_fk FOREIGN KEY (smediafileid)
      REFERENCES wbsc.smediafiles (smediafileid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;

-- evalanswers
ALTER TABLE wbsc.evalanswers
  ADD COLUMN smediafileid integer;

ALTER TABLE wbsc.evalanswers
  ADD CONSTRAINT evalanswers_smediafileid_fk FOREIGN KEY (smediafileid)
      REFERENCES wbsc.smediafiles (smediafileid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
