INSERT INTO wbsc.rights (name) VALUES ('DATA_EXPORT_ALL_RESULTS');

WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'DATA_EXPORT_ALL_RESULTS'),
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
                              ON rights.rightid = rr.rightid AND rights.name = 'DATA_EXPORT_ALL_RESULTS'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;
