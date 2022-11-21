-- remove the right "View all Evaluations" from "Supervisor" role
DELETE FROM wbsc.role_right
WHERE rightid=(SELECT rightid from wbsc.rights where name='VIEW_ALL_EVALS')
AND roleid=(SELECT roleid FROM wbsc.roles WHERE name='Supervisor');
