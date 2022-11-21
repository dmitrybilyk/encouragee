INSERT INTO wbsc.rights (name) VALUES ('ADVANCED_SEARCH_USER');

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='ADVANCED_SEARCH_USER'),
     roleids AS
    (SELECT roleid FROM wbsc.roles WHERE roleid NOT IN
--  IT Administrator
    (SELECT roleid
      FROM wbsc.roles roles_main
      WHERE EXISTS(
                SELECT 1
                FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                  INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_ALL_PASSWORD'
                                                   AND roles_main.roleid = rr.roleid
            ) AND NOT EXISTS(SELECT 1
                         FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                           INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'ADVANCED_SEARCH_USER'
                                                            AND roles_main.roleid = rr.roleid)))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids, roleids;

