CREATE DOMAIN callrec.users_authmethod AS VARCHAR
  CONSTRAINT users_authmethod_ck CHECK (
       VALUE IN (
         'SQL',
         'LDAP'
       )
  );

