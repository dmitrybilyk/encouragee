insert into wbsc.interaction_tags (tagtext, privatetag, datetime, creatorid, creatorname, companyid)
select 'Bad Audio', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE userid=
-- CC Manager user
(SELECT min(users.userid)
FROM wbsc.sc_users users
  INNER JOIN wbsc.user_role ur ON users.userid = ur.userid
WHERE roleid =
      (SELECT min(roleid) AS roleid
       FROM wbsc.roles roles_main
       WHERE EXISTS(
                 SELECT 1
                 FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                   INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_TEAM_PASSWORD'
                                                    AND roles_main.roleid = rr.roleid
             ) AND EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                 AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EDIT_INTERACTION_TYPES'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EVAL_REMOVE_ALL'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'VIEW_TEAM_EVALS'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_ROLES'
                                                           AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'PLAN_ALL_EVALS'
                               AND roles_main.roleid = rr.roleid)));
select 'Bad Audio', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE login='ccmanager';

insert into wbsc.interaction_tags (tagtext, privatetag, datetime, creatorid, creatorname, companyid)
select 'Angry Customer', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE userid=
-- CC Manager user
(SELECT min(users.userid)
FROM wbsc.sc_users users
  INNER JOIN wbsc.user_role ur ON users.userid = ur.userid
WHERE roleid =
      (SELECT min(roleid) AS roleid
       FROM wbsc.roles roles_main
       WHERE exists(
                 SELECT 1
                 FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                   INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_TEAM_PASSWORD'
                                                    AND roles_main.roleid = rr.roleid
             ) AND EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                 AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EDIT_INTERACTION_TYPES'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EVAL_REMOVE_ALL'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'VIEW_TEAM_EVALS'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_ROLES'
                                                           AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'PLAN_ALL_EVALS'
                               AND roles_main.roleid = rr.roleid)));
select 'Angry Customer', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE login='ccmanager';

insert into wbsc.interaction_tags (tagtext, privatetag, datetime, creatorid, creatorname, companyid)
select 'Investigate', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE userid=
-- CC Manager user
(SELECT min(users.userid)
FROM wbsc.sc_users users
  INNER JOIN wbsc.user_role ur ON users.userid = ur.userid
WHERE roleid =
      (SELECT min(roleid) AS roleid
       FROM wbsc.roles roles_main
       WHERE exists(
                 SELECT 1
                 FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                   INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_TEAM_PASSWORD'
                                                    AND roles_main.roleid = rr.roleid
             ) AND EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                 AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EDIT_INTERACTION_TYPES'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EVAL_REMOVE_ALL'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'VIEW_TEAM_EVALS'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_ROLES'
                                                           AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'PLAN_ALL_EVALS'
                               AND roles_main.roleid = rr.roleid)));
select 'Investigate', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE login='ccmanager';

insert into wbsc.interaction_tags (tagtext, privatetag, datetime, creatorid, creatorname, companyid)
select 'Excellent', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE userid=
-- CC Manager user
(SELECT min(users.userid)
FROM wbsc.sc_users users
  INNER JOIN wbsc.user_role ur ON users.userid = ur.userid
WHERE roleid =
      (SELECT min(roleid) AS roleid
       FROM wbsc.roles roles_main
       WHERE exists(
                 SELECT 1
                 FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                   INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_TEAM_PASSWORD'
                                                    AND roles_main.roleid = rr.roleid
             ) AND EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                 AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EDIT_INTERACTION_TYPES'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EVAL_REMOVE_ALL'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'VIEW_TEAM_EVALS'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_ROLES'
                                                           AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'PLAN_ALL_EVALS'
                               AND roles_main.roleid = rr.roleid)));
select 'Excellent', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE login='ccmanager';

insert into wbsc.interaction_tags (tagtext, privatetag, datetime, creatorid, creatorname, companyid)
select 'Calibration', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE userid=
-- CC Manager user
(SELECT min(users.userid)
FROM wbsc.sc_users users
  INNER JOIN wbsc.user_role ur ON users.userid = ur.userid
WHERE roleid =
      (SELECT min(roleid) AS roleid
       FROM wbsc.roles roles_main
       WHERE exists(
                 SELECT 1
                 FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                   INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_TEAM_PASSWORD'
                                                    AND roles_main.roleid = rr.roleid
             ) AND EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                 AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EDIT_INTERACTION_TYPES'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EVAL_REMOVE_ALL'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'VIEW_TEAM_EVALS'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_ROLES'
                                                           AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'PLAN_ALL_EVALS'
                               AND roles_main.roleid = rr.roleid)));
select 'Calibration', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE login='ccmanager';

insert into wbsc.interaction_tags (tagtext, privatetag, datetime, creatorid, creatorname, companyid)
select 'Training', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE userid=
-- CC Manager user
(SELECT min(users.userid)
FROM wbsc.sc_users users
  INNER JOIN wbsc.user_role ur ON users.userid = ur.userid
WHERE roleid =
      (SELECT min(roleid) AS roleid
       FROM wbsc.roles roles_main
       WHERE exists(
                 SELECT 1
                 FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                   INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'RESET_TEAM_PASSWORD'
                                                    AND roles_main.roleid = rr.roleid
             ) AND EXISTS(SELECT 1
                          FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                            INNER JOIN wbsc.rights rights
                              ON rights.rightid = rr.rightid AND rights.name = 'EDIT_ADMIN_SETTING'
                                 AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EDIT_INTERACTION_TYPES'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'EVAL_REMOVE_ALL'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'VIEW_TEAM_EVALS'
                               AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'MANAGE_ROLES'
                                                           AND roles_main.roleid = rr.roleid)
             AND EXISTS(SELECT 1
                        FROM wbsc.roles roles JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights
                            ON rights.rightid = rr.rightid AND rights.name = 'PLAN_ALL_EVALS'
                               AND roles_main.roleid = rr.roleid)));
select 'Training', 'f', to_date('24/07/2015','dd/MM/yyyy'), userid, name, company FROM wbsc.sc_users WHERE login='ccmanager';
