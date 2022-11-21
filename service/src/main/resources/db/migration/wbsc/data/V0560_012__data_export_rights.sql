INSERT INTO wbsc.rights (name) VALUES ('DATA_TASK');
INSERT INTO wbsc.rights (name) VALUES ('DATA_TASK_ALL');

WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'DATA_TASK'),
    roleids AS
    --       Team leader role
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE EXISTS(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid
           WHERE rights.name = 'MANAGE_ASSIGNED_EVALUATIONS'
                 AND roles_main.roleid = rr.roleid)
         AND NOT EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid
                        WHERE rights.name = 'DATA_TASK'
                              AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;

WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'DATA_TASK_ALL'),
    roleids AS
    --       CC Manager role
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'DATA_TASK_ALL'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;
