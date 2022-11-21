INSERT INTO wbsc.rights (name) VALUES ('REALLOCATE_EVALUATIONS');

WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'REALLOCATE_EVALUATIONS'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE EXISTS(
           SELECT 1
           FROM wbsc.roles roles
             JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             JOIN wbsc.rights rights ON rights.rightid = rr.rightid
           WHERE rights.name = 'PLAN_ALL_EVALS'
                 AND roles_main.roleid = rr.roleid)
         AND NOT EXISTS(
           SELECT 1
           FROM wbsc.roles roles
             JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             JOIN wbsc.rights rights ON rights.rightid = rr.rightid
           WHERE rights.name = 'REALLOCATE_EVALUATIONS'
                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;
