ALTER TABLE callrec.users_audit DROP CONSTRAINT users_audit_pkey;
ALTER TABLE callrec.users_audit ADD CONSTRAINT users_audit_pkey PRIMARY KEY (user_auditid);
