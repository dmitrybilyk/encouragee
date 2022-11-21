-- SC-6580
INSERT INTO wbsc.rights (name) VALUES ('VIEW_DATA_WAREHOUSE_REPORTS');

-- assign to the roles which have already VIEW_ALL_REPORTS
WITH roleids AS
    (SELECT ROLES.roleid
     FROM wbsc.roles ROLES
     JOIN wbsc.role_right ROLERIGHTS ON ROLES.roleid = ROLERIGHTS.roleid
     JOIN wbsc.rights RIGHTS ON ROLERIGHTS.rightid = RIGHTS.rightid
     WHERE RIGHTS.name = 'VIEW_ALL_REPORTS'),
     rightids AS
    (SELECT rightid
     FROM wbsc.rights
     WHERE name = 'VIEW_DATA_WAREHOUSE_REPORTS')
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;