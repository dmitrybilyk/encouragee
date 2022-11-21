INSERT INTO wbsc.rights (name) VALUES ('VIEW_CONVERSATIONS_SCREEN');

-- assign new right to every role - including custom roles
WITH view_conversations_rightid AS (
    SELECT rightid
    FROM wbsc.rights
    WHERE name = 'VIEW_CONVERSATIONS_SCREEN'
  ), roleids AS (
    SELECT roles.roleid
    FROM wbsc.roles roles
  )
INSERT INTO wbsc.role_right (rightid, roleid)
  SELECT *
  FROM view_conversations_rightid, roleids;
