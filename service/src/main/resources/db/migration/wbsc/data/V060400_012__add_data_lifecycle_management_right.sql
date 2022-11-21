INSERT INTO wbsc.rights (name) VALUES ('MANAGE_DATA_LIFECYCLE');

-- assign new right to current ccmanager equivalent roles
WITH rightids AS (
    SELECT rightid
    FROM wbsc.rights
    WHERE name = 'MANAGE_DATA_LIFECYCLE'
  ), roleids AS (
    SELECT roles.roleid
    FROM wbsc.roles roles
    JOIN wbsc.role_right roleright ON roles.roleid = roleright.roleid
    JOIN wbsc.rights ON rights.rightid = roleright.rightid
    WHERE rights.name = 'EDIT_ADMIN_SETTING'
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;
