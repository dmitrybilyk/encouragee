INSERT INTO wbsc.rights (name) VALUES ('REPORTS_SPEECH_TAGS_OCCURRENCES');

INSERT INTO wbsc.role_right (rightid, roleid)
with
    q1 as (SELECT RIGHTID FROM wbsc.RIGHTS WHERE NAME = 'REPORTS_SPEECH_TAGS_OCCURRENCES'),
    q2 AS
        --       CC Manager role
        (SELECT roleid
         FROM wbsc.roles roles_main
         WHERE exists(
                 SELECT 1
                 FROM wbsc.roles roles
                          JOIN wbsc.role_right rr ON roles.roleid = rr.roleid
                          INNER JOIN wbsc.rights rights ON rights.rightid = rr.rightid AND rights.name = 'EDIT_GLOBAL_TEMPLATES'
                     AND roles_main.roleid = rr.roleid
             )
        )
SELECT
    q1.RIGHTID, q2.ROLEID
FROM
    q1, q2;
