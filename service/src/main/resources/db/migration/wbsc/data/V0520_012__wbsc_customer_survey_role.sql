INSERT INTO wbsc.roles (name, description, company) VALUES ('Customer Survey', 'Provide customer surveys and store as evaluations', (SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'));

INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='ADD_CALL'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='CREATE_EVALS'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='EVAL_AGENTS'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='PLAN_ALL_EVALS'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='REOPEN_EVALS'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='REPLACE_CALLS'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='VIEW_MY_EVALS'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((SELECT rightid from wbsc.rights where name='VIEW_TEAM_EVALS'), (SELECT roleid FROM wbsc.roles WHERE name='Customer Survey'));
