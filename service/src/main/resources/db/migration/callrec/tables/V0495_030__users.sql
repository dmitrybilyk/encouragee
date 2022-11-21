-- CREATE TABLE users {{{
-->>
-- <code>authmethod</code>: SEE domains/users_authmethod.sql for
-- list of accepted values
--
-- SEE ALSO: domains/users_authmethod.sql, domains/emailaddrspec.sql,
-- views/users.sql, tables/user_roles.sql,
-- functions/unixtstamp_to_tstamp.sql, functions/tstamp_to_unixtstamp.sql
--<<
CREATE TABLE callrec.users
(
  id integer NOT NULL,
  "login" character varying(255) NOT NULL,
  authmethod callrec.users_authmethod NOT NULL  DEFAULT 'SQL'::character varying,
  "password" callrec.md5_d,
  firstname character varying(255),
  lastname character varying(255),
  email callrec.emailaddrspec,
  phonenr text,
  config text,
  created_ts timestamp with time zone NOT NULL DEFAULT now(),
  view_restrictionid integer,
  CONSTRAINT _users_pk PRIMARY KEY (id),
  CONSTRAINT _view_restriction_user_fk FOREIGN KEY (view_restrictionid)
      REFERENCES callrec.view_restrictions (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT _users_login_key UNIQUE (login)
)
WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.users TO GROUP callrecgrp;


ALTER TABLE callrec.users OWNER TO postgres;

--CAL-6104
ALTER TABLE callrec.users ALTER COLUMN config SET DEFAULT
'<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <SpecifiedConfiguration name="columns">
      <Group name="main_columns">
        <Value name="main_date">true</Value>
        <Value name="main_beginning">true</Value>
        <Value name="main_end">false</Value>
        <Value name="main_length">false</Value>
        <Value name="main_from">true</Value>
        <Value name="main_to">true</Value>
        <Value name="main_desc">true</Value>
      </Group>
      <Group name="lm_main_columns">
        <Value name="duration">true</Value>
        <Value name="callingNumber">true</Value>
        <Value name="calledNumber">true</Value>
        <Value name="recordStatus">true</Value>
      </Group>
   </SpecifiedConfiguration>
</Configuration>'::text;

-- }}}

