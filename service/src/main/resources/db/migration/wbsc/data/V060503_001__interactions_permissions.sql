INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_ANONYMIZE');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_TAGS_ADD');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_TAGS_EDIT');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_TAGS_VIEW');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTION_REVIEWS_VIEW');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTIONS_EXPORT_JSON');


WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'INTERACTION_ANONYMIZE'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name IN ('MANAGE_COMPLIANCE')
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'INTERACTION_ANONYMIZE'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;


WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'INTERACTION_TAGS_ADD'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name IN ('INTERACTIONS_FULL_VIEW', 'INTERACTIONS_GROUP_VIEW')
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'INTERACTION_TAGS_ADD'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;


WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'INTERACTION_TAGS_EDIT'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name IN ('INTERACTIONS_FULL_VIEW')
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'INTERACTION_TAGS_EDIT'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;


WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'INTERACTION_TAGS_VIEW'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name IN ('INTERACTIONS_AGENT_VIEW', 'INTERACTION_TAGS_ADD')
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'INTERACTION_TAGS_VIEW'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;


WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'INTERACTION_REVIEWS_VIEW'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name IN ('INTERACTIONS_FULL_VIEW', 'INTERACTIONS_GROUP_VIEW')
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'INTERACTION_REVIEWS_VIEW'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;


WITH rightids AS
(SELECT rightid
 FROM wbsc.rights
 WHERE name = 'INTERACTIONS_EXPORT_JSON'),
    roleids AS
  (SELECT roleid
   FROM wbsc.roles roles_main
   WHERE exists(
           SELECT 1
           FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
             INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name IN ('INTERACTIONS_FULL_VIEW')
                                              AND roles_main.roleid = rr.roleid
         ) AND NOT EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'INTERACTIONS_EXPORT_JSON'
                                 AND roles_main.roleid = rr.roleid)
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM rightids, roleids;