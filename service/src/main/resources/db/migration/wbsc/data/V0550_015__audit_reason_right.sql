INSERT INTO wbsc.rights (name) VALUES ('ADD_AUDIT_REASON');
INSERT INTO wbsc.rights (name) VALUES ('EDIT_AUDIT_REASON');

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='ADD_AUDIT_REASON'),
     roleids AS
    (SELECT roleid FROM wbsc.roles WHERE roleid in (
--    CC Manager role
      SELECT roleid
        FROM wbsc.roles roles_main
        WHERE EXISTS(
                  SELECT 1
                  FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                    INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                                     AND roles_main.roleid = rr.roleid
              AND NOT EXISTS(SELECT 1
                         FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                           INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'ADD_AUDIT_REASON'
                                                            AND roles_main.roleid = rr.roleid)))
              OR roleid in
--    Supervisor role
      (SELECT roleid
        FROM wbsc.roles roles_main
        WHERE EXISTS(
                  SELECT 1
                  FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                    INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_TEAM_PASSWORD'
                                                     AND roles_main.roleid = rr.roleid
              ) AND NOT EXISTS(SELECT 1
                             FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                               INNER JOIN wbsc.rights rights
                                 ON rights.rightid = rr.rightid AND rights.name = 'ADD_AUDIT_REASON'
                                    AND roles_main.roleid = rr.roleid))
                                    OR roleid in
--    Team leader role
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
                           INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'ADD_AUDIT_REASON'
                                                            AND roles_main.roleid = rr.roleid)
                    ))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='EDIT_AUDIT_REASON'),
     roleids AS
    (SELECT roleid FROM wbsc.roles WHERE roleid in
--     CC Manager role
    (SELECT roleid
        FROM wbsc.roles roles_main
        WHERE EXISTS(
                  SELECT 1
                  FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                    INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                                     AND roles_main.roleid = rr.roleid
              ) AND NOT EXISTS(SELECT 1
                         FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                           INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'EDIT_AUDIT_REASON'
                                                            AND roles_main.roleid = rr.roleid)
            ))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids, roleids;
