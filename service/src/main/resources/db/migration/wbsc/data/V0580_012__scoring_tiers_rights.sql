INSERT INTO wbsc.rights (name) VALUES ('SCORING_TIERS_VIEW');

WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'SCORING_TIERS_VIEW'),
    roleids AS
    --       CC Manager role
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
             SELECT 1
             FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
               INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_QUESTIONNAIRES'
                                                AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'SCORING_TIERS_VIEW'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;


INSERT INTO wbsc.rights (name) VALUES ('SCORING_TIERS_MANAGE');

WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'SCORING_TIERS_MANAGE'),
    roleids AS
    --       CC Manager role
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
             SELECT 1
             FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
               INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_QUESTIONNAIRES'
                                                AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'SCORING_TIERS_MANAGE'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;
