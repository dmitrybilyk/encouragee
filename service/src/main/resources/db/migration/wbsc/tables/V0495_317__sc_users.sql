-- Table: wbsc.sc_users

-- DROP TABLE wbsc.sc_users;

CREATE TABLE wbsc.sc_users
(
  userid integer NOT NULL DEFAULT nextval('wbsc.seq_users'::regclass),
  "name" character varying(50) NOT NULL,
  surname character varying(50) NOT NULL,
  "login" character varying(50) NOT NULL, -- login name
  "database" integer NOT NULL, -- pointer to a database pool
  sync boolean NOT NULL DEFAULT true, -- this user can be synchronized with IPCC
  status wbsc.user_status_domain NOT NULL,
  phone character varying, -- phone number
  agentid character varying, -- ucc identifator
  identificator_used wbsc.user_identificator_domain NOT NULL, -- phone or uccid is used for searching
  delegator integer, -- if you give rights to do your job to someone else while on holidays...
  delegation_from timestamp(0) with time zone,
  delegation_to timestamp(0) with time zone,
  send_feedback integer, -- Whom will your feedback send to.
  email character varying(255),
  "language" integer NOT NULL,
  company integer NOT NULL,
  external_id character varying(255),
  deactivation_date timestamp(0) with time zone,
  created_ts timestamp(0) with time zone DEFAULT now(),
  deleted_ts timestamp(0) with time zone,
  locked_ts timestamp(0) with time zone,
  CONSTRAINT users_pkey PRIMARY KEY (userid),
  CONSTRAINT database_fk FOREIGN KEY ("database")
      REFERENCES wbsc."database" (databaseid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT language_fk FOREIGN KEY ("language")
      REFERENCES wbsc.languages (languageid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT users_company_fk FOREIGN KEY (company)
      REFERENCES wbsc.companies (companyid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT users_delegator_fk FOREIGN KEY (delegator)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT users_send_feedback_fk FOREIGN KEY (send_feedback)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT users_login_key UNIQUE (login,company)
)
WITHOUT OIDS;
ALTER TABLE wbsc.sc_users OWNER TO postgres;
COMMENT ON TABLE wbsc.sc_users IS 'holds information about all users of scorecard';
COMMENT ON COLUMN wbsc.sc_users."login" IS 'login name';
COMMENT ON COLUMN wbsc.sc_users."database" IS 'pointer to a database pool';
COMMENT ON COLUMN wbsc.sc_users.sync IS 'this user can be synchronized with IPCC';
COMMENT ON COLUMN wbsc.sc_users.phone IS 'phone number';
COMMENT ON COLUMN wbsc.sc_users.agentid IS 'ucc identifator';
COMMENT ON COLUMN wbsc.sc_users.identificator_used IS 'phone or uccid is used for searching';
COMMENT ON COLUMN wbsc.sc_users.delegator IS 'if you give rights to do your job to someone else while on holidays...';
COMMENT ON COLUMN wbsc.sc_users.send_feedback IS 'Whom will your feedback send to.';
COMMENT ON COLUMN wbsc.sc_users.locked_ts IS 'Timestamp when user account was locked.';

GRANT select, update, insert, delete on wbsc.sc_users to wbscgrp;

