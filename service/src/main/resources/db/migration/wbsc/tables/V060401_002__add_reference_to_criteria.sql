DELETE FROM wbsc.medialimits a WHERE criteriaid IS NULL OR
NOT EXISTS (SELECT 1 FROM wbsc.criteria b WHERE b.criteriaid = a.criteriaid);

ALTER TABLE wbsc.medialimits
ADD CONSTRAINT sc_medialimits_criteriaid_fk FOREIGN KEY (criteriaid)
    REFERENCES wbsc.criteria (criteriaid) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE CASCADE;


DELETE FROM wbsc.subevaluation a WHERE criteriaid IS NULL OR
NOT EXISTS (SELECT 1 FROM wbsc.criteria b WHERE b.criteriaid = a.criteriaid);

ALTER TABLE wbsc.subevaluation
ADD CONSTRAINT sc_subevaluation_criteriaid_fk FOREIGN KEY (criteriaid)
    REFERENCES wbsc.criteria (criteriaid) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE CASCADE;
