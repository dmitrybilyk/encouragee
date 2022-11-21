INSERT INTO wbsc.rights (name) VALUES ('MANAGE_COMPLIANCE');

INSERT INTO wbsc.roles (name, description, company)
VALUES ('Compliance Analyst',
        'Perform and manage compliance related activities',
        (SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));

-- add rights to new role
INSERT INTO wbsc.role_right (rightid, roleid)
  VALUES ((SELECT rightid FROM wbsc.rights WHERE name = 'MANAGE_COMPLIANCE'), (SELECT roleid FROM wbsc.roles WHERE name='Compliance Analyst'));
INSERT INTO wbsc.role_right (rightid, roleid)
  VALUES ((SELECT rightid FROM wbsc.rights WHERE name = 'INTERACTIONS_FULL_VIEW'), (SELECT roleid FROM wbsc.roles WHERE name='Compliance Analyst'));
INSERT INTO wbsc.role_right (rightid, roleid)
  VALUES ((SELECT rightid FROM wbsc.rights WHERE name = 'VIEW_AUDIT'), (SELECT roleid FROM wbsc.roles WHERE name='Compliance Analyst'));

--assign new right to current ccmanager equivalent roles
WITH rightids AS
  (SELECT rightid
  FROM wbsc.rights
  WHERE name = 'MANAGE_COMPLIANCE'),
    roleids AS
    --       CC Manager role
  (SELECT roleid
  FROM wbsc.roles roles_main
  WHERE exists(
             SELECT 1
             FROM wbsc.roles roles
              JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
              INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                            AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles
                            JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_COMPLIANCE'
                                                          AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;
