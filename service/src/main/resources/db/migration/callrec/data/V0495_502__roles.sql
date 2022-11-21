INSERT INTO callrec.roles (
  id,
  parentid,
  name,
  phonenrs,
  created_ts
) VALUES (
  callrec.GET_NEXT_ROLEID(),
  NULL,
  'Admin',
  '',
  NOW()
  );
  
INSERT INTO callrec.roles (
  id,
  parentid,
  name,
  phonenrs,
  created_ts
) VALUES (
  callrec.GET_NEXT_ROLEID(),
  (select id from callrec.roles WHERE name='Admin'),
  'System_play',
  '',
  NOW()
  );  
