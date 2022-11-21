-- CAL-11187, templates cannot break import if users or groups are changed in the outside source

DELETE FROM wbsc.template_for_group WHERE evaluator IN (
  SELECT evaluator
  FROM wbsc.template_for_group
  WHERE evaluator NOT IN (
    SELECT userid FROM wbsc.sc_users
  )
);
ALTER TABLE wbsc.template_for_group DROP CONSTRAINT sc_template_for_groups_evaluator;
ALTER TABLE wbsc.template_for_group ADD CONSTRAINT sc_template_for_groups_evaluator FOREIGN KEY (evaluator)
    REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE;


DELETE FROM wbsc.template_for_group WHERE ccgroupid IN (
  SELECT ccgroupid
  FROM wbsc.template_for_group
  WHERE ccgroupid NOT IN (
    SELECT ccgroupid FROM wbsc.ccgroups
  )
);
ALTER TABLE wbsc.template_for_group DROP CONSTRAINT sc_template_for_groups_ccgroup;
ALTER TABLE wbsc.template_for_group ADD CONSTRAINT sc_template_for_groups_ccgroup FOREIGN KEY (ccgroupid)
	REFERENCES wbsc.ccgroups (ccgroupid) MATCH SIMPLE
	ON UPDATE CASCADE ON DELETE CASCADE;


DELETE FROM wbsc.template_for_user WHERE evaluator IN (
  SELECT evaluator
  FROM wbsc.template_for_user
  WHERE evaluator NOT IN (
    SELECT userid FROM wbsc.sc_users
  )
);
ALTER TABLE wbsc.template_for_user DROP CONSTRAINT sc_template_for_user_evaluator;
ALTER TABLE wbsc.template_for_user ADD CONSTRAINT sc_template_for_user_evaluator FOREIGN KEY (evaluator)
    REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE;


DELETE FROM wbsc.template_for_user WHERE templateid IN (
  SELECT templateid
  FROM wbsc.template_for_user
  WHERE templateid NOT IN (
    SELECT templateid FROM wbsc.templates
  )
);
ALTER TABLE wbsc.template_for_user DROP CONSTRAINT sc_template_for_user_template;
ALTER TABLE wbsc.template_for_user ADD CONSTRAINT sc_template_for_user_template FOREIGN KEY (templateid)
    REFERENCES wbsc.templates (templateid) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE;


DELETE FROM wbsc.template_for_user WHERE userid IN (
  SELECT userid
  FROM wbsc.template_for_user
  WHERE userid NOT IN (
    SELECT userid FROM wbsc.sc_users
  )
);
ALTER TABLE wbsc.template_for_user DROP CONSTRAINT sc_template_for_user_userid;
ALTER TABLE wbsc.template_for_user ADD CONSTRAINT sc_template_for_user_userid FOREIGN KEY (userid)
    REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE;
