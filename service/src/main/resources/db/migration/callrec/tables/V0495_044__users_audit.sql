CREATE TABLE callrec.users_audit
(
  user_auditid integer NOT NULL DEFAULT nextval('callrec.seq_useraudit'::regclass),
  "login" varchar(255) NOT NULL,
  "action" varchar(255) NOT NULL,
  action_result varchar(15),
  action_ts timestamptz NOT NULL DEFAULT now(),
  message text,
  CONSTRAINT users_audit_pkey PRIMARY KEY (action_ts),
  CONSTRAINT empty_login_check CHECK (login::text <> ''::text)

)
WITHOUT OIDS;
ALTER TABLE callrec.users_audit OWNER TO postgres;
GRANT ALL ON TABLE callrec.users_audit TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.users_audit TO callrecgrp;

COMMENT ON TABLE callrec.users_audit IS 'Login as unique identification of user and his action are stored here. ';
