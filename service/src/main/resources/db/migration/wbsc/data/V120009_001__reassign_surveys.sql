WITH ccmanager AS (
  INSERT INTO
    wbsc.sc_users (
  	  name, surname, login, DATABASE, sync, status,
      identificator_used, LANGUAGE, company
  )
  VALUES (
	'CcManager', 'CcManager', 'ccmanager', (SELECT databaseid FROM wbsc.DATABASE WHERE name='ScoreCardDB'), TRUE, 'ACTIVE',
  	'NONE', 1, (SELECT companyid from wbsc.companies where display_name = 'DEFAULT')
  )
  ON CONFLICT ON CONSTRAINT users_login_key DO UPDATE SET login = 'ccmanager'
  RETURNING userid
), belongstogroup AS (
  INSERT INTO
    wbsc.user_belongsto_ccgroup (
      userid, ccgroupid
    )
  VALUES (
    (SELECT userid FROM ccmanager), (SELECT ccgroupid FROM wbsc.ccgroups WHERE ccgroupname = 'QM_Root_Group_Original')
  )
  ON CONFLICT ON CONSTRAINT user_belongsto_ccgroup_pkey DO NOTHING
)

UPDATE wbsc.evaluations
  SET evaluatorid = (SELECT userid FROM ccmanager)
  WHERE qformid IN (SELECT qformid FROM wbsc.questforms WHERE qftype = 'SURVEY');
