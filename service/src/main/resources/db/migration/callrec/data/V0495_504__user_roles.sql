INSERT INTO callrec.user_roles (
  userid,
  roleid
) VALUES (
  (SELECT id FROM callrec.users WHERE login = 'admin'),
  (SELECT id FROM callrec.roles WHERE parentid IS NULL)
);

INSERT INTO callrec.user_roles (
  userid,
  roleid
) VALUES (
  (SELECT id FROM callrec.users WHERE login = 'scorecard'),
  (SELECT id FROM callrec.roles WHERE name='System_play')
);

INSERT INTO callrec.role_actions (
	roleid, 
	actionid
	)
  SELECT (SELECT id FROM callrec.roles WHERE name = 'System_play'),id
    FROM callrec.actions
   WHERE name not IN ('call_all', 'export_call', 'video_call');
