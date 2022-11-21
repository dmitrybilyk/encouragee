WITH rightid AS
    (SELECT rightid
     FROM wbsc.rights
     WHERE name = 'EDIT_ADMIN_SETTING'),
    roleids AS
    (SELECT ro.roleid
     FROM wbsc.role_right rr, wbsc.rights ri, wbsc.roles ro
     WHERE rr.rightid = ri.rightid
         AND ro.roleid = rr.roleid
         AND ri.name = 'INTERACTIONS_FULL_VIEW'
         AND NOT rr.roleid IN
             (SELECT ro.roleid
              FROM wbsc.role_right rr, wbsc.rights ri, wbsc.roles ro
              WHERE rr.rightid = ri.rightid
                  AND ro.roleid = rr.roleid
                  AND ri.name = 'EDIT_ADMIN_SETTING'))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightid, roleids;


WITH rightid AS
    (SELECT rightid
     FROM wbsc.rights
     WHERE name = 'INTERACTIONS_EXPORT_MEDIA'),
    roleids AS
    (SELECT ro.roleid
     FROM wbsc.role_right rr, wbsc.rights ri, wbsc.roles ro
     WHERE rr.rightid = ri.rightid
         AND ro.roleid = rr.roleid
         AND ri.name = 'INTERACTIONS_FULL_VIEW'
         AND NOT rr.roleid IN
             (SELECT ro.roleid
              FROM wbsc.role_right rr, wbsc.rights ri, wbsc.roles ro
              WHERE rr.rightid = ri.rightid
                  AND ro.roleid = rr.roleid
                  AND ri.name = 'INTERACTIONS_EXPORT_MEDIA'))
INSERT INTO wbsc.role_right (rightid, roleid)
SELECT *
FROM rightid, roleids;