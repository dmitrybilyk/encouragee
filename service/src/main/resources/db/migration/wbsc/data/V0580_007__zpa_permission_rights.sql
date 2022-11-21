INSERT INTO wbsc.rights (name) VALUES ('VIEW_ZPA');

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='VIEW_ZPA'),
     roleids AS
    (SELECT roleid FROM wbsc.roles WHERE roleid IN
--  CC Manager
    (SELECT roleid
     FROM wbsc.roles roles_main
     WHERE EXISTS(
                SELECT 1
                FROM wbsc.roles roles
                JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                           AND roles_main.roleid = rr.roleid
            ) AND NOT EXISTS(
                SELECT 1
			          FROM wbsc.roles roles
                JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'VIEW_ZPA'
                                           AND roles_main.roleid = rr.roleid)))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids, roleids;