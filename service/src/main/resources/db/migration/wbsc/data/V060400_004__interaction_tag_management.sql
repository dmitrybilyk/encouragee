INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_TAGS_MANAGE');

-- add the new right to role ccmanager and compliance analyst roles
WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='INTERACTION_TAGS_MANAGE'),
     roleids AS
    (SELECT roleid FROM wbsc.roles WHERE name in('CC Manager', 'Compliance Analyst'))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;
