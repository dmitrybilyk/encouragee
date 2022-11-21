INSERT INTO wbsc.rights (name) VALUES ('REPORTING_ALL');
INSERT INTO wbsc.rights (name) VALUES ('REPORTING_ASSIGNED_TEAMS');
INSERT INTO wbsc.rights (name) VALUES ('REPORTING_OWN_TEAM');
INSERT INTO wbsc.rights (name) VALUES ('REPORTING_SELF');

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='REPORTING_ALL'),
     roleids AS
--  CC Manager
    (SELECT roleid
        FROM wbsc.roles roles_main
        WHERE EXISTS(
                  SELECT 1
                  FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                    INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'EDIT_ADMIN_SETTING'
                                                     AND roles_main.roleid = rr.roleid
              ) AND NOT EXISTS(SELECT 1
                         FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                           INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'REPORTING_ALL'
                                                            AND roles_main.roleid = rr.roleid))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='REPORTING_ASSIGNED_TEAMS'),
     roleids AS
--  Supervisor role
    (SELECT roleid
        FROM wbsc.roles roles_main
        WHERE EXISTS(
                  SELECT 1
                  FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                    INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'RESET_TEAM_PASSWORD'
                                                     AND roles_main.roleid = rr.roleid
              ) AND NOT EXISTS(SELECT 1
                             FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                               INNER JOIN wbsc.rights rights
                                 ON rights.rightid = rr.rightid AND rights.name = 'REPORTING_ASSIGNED_TEAMS'
                                    AND roles_main.roleid = rr.roleid)
            )
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='REPORTING_OWN_TEAM'),
     roleids AS
--      Team leader role
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
                           INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'REPORTING_OWN_TEAM'
                                                            AND roles_main.roleid = rr.roleid)
            )
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids, roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='REPORTING_SELF'),
     roleids AS
--      Agent role
        (SELECT roleid
            FROM wbsc.roles roles_main
            WHERE EXISTS(
                SELECT 1
                FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                  INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'INTERACTIONS_AGENT_VIEW'
                                                   AND roles_main.roleid = rr.roleid)
                  AND NOT EXISTS(
                    SELECT 1
                    FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                    INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'REPORTING_SELF'
                                                   AND roles_main.roleid = rr.roleid)
                )
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='REPORTING_SELF'),
     roleids AS
--      Supervisor role
        (SELECT roleid
            FROM wbsc.roles roles_main
            WHERE EXISTS(
                      SELECT 1
                      FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                        INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'RESET_TEAM_PASSWORD'
                                                         AND roles_main.roleid = rr.roleid
                  )
                  AND NOT EXISTS(SELECT 1
                                 FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                                   INNER JOIN wbsc.rights rights
                                     ON rights.rightid = rr.rightid WHERE rights.name = 'REPORTING_SELF'
                                        AND roles_main.roleid = rr.roleid)
            )
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='REPORTING_SELF'),
     roleids AS
--  Team leader
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
                           INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'REPORTING_SELF'
                                                            AND roles_main.roleid = rr.roleid)
          )
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids, roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='REPORTING_SELF'),
     roleids AS
--      CC Manager role
        (SELECT roleid
            FROM wbsc.roles roles_main
            WHERE EXISTS(
                      SELECT 1
                      FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                        INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'EDIT_ADMIN_SETTING'
                                                         AND roles_main.roleid = rr.roleid
                  )
                  AND NOT EXISTS(SELECT 1
                             FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                               INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid WHERE rights.name = 'REPORTING_SELF'
                                                                AND roles_main.roleid = rr.roleid))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids, roleids;
