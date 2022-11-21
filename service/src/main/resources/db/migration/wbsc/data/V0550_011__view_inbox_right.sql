-- SC-6922
INSERT INTO  wbsc.rights (name) VALUES ('VIEW_INBOX');

-- assign VIEW_INBOX to all roles
WITH roleids AS
    (SELECT ROLES.roleid
     FROM wbsc.roles ROLES),
     rightids AS
    (SELECT rightid
     FROM wbsc.rights
     WHERE name = 'VIEW_INBOX')
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;