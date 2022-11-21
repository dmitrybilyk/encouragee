INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_TAGS_EDIT');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_TAGS_ADD');

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='INTERACTION_TAGS_ADD'),
     roleids AS
    (SELECT roleid FROM wbsc.roles WHERE name in('CC Manager', 'Supervisor', 'Team leader'))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;

WITH rightids AS
    (SELECT rightid FROM wbsc.rights WHERE name='INTERACTION_TAGS_EDIT'),
     roleids AS
    (SELECT roleid FROM wbsc.roles WHERE name='CC Manager')
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightids,
     roleids;