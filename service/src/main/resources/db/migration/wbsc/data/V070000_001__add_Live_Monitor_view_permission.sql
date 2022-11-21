INSERT INTO wbsc.rights (name) VALUES ('LIVE_MONITOR_VIEW');

WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'LIVE_MONITOR_VIEW'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name IN ('EVAL_AGENTS')
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'LIVE_MONITOR_VIEW'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;
