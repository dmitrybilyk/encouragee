WITH rightids AS
  (SELECT rightid from wbsc.rights where name='MANAGE_QUESTIONNAIRES'),
     roleids AS (
SELECT roleid
FROM wbsc.roles roles_main
WHERE EXISTS(
          SELECT 1
          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
            INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'REOPEN_EVALS'
                                             AND roles_main.roleid = rr.roleid
      )
      AND NOT EXISTS(SELECT 1
                     FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                       INNER JOIN wbsc.rights rights
                         ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_QUESTIONNAIRES'
                            AND roles_main.roleid = rr.roleid))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;
