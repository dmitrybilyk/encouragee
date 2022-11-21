-- CREATE TABLE roles {{{
-->>
-- A role (or group) is a set of users.
--
-- <code>phonenrs</code>: semicolon-separated list of phone number masks
-- this role has access to.
--
-- SEE ALSO: views/roles.sql, tables/users.sql, views/users.sql,
-- tables/user_roles.sql, tables/role_actions.sql,
-- rules/roles_delete.sql, rules/roles_insert.sql, rules/roles_update.sql,
-- functions/unixtstamp_to_tstamp.sql, functions/tstamp_to_unixtstamp.sql
--<<
CREATE TABLE callrec.roles
(
  id integer NOT NULL,
  parentid integer,
  "name" character varying(100) NOT NULL,
  config text,
  phonenrs text,
  description text DEFAULT ''::text,
  created_ts timestamp with time zone NOT NULL DEFAULT now(),
  view_restrictionid integer,
  CONSTRAINT _roles_pk PRIMARY KEY (id),
  CONSTRAINT _roles_parentid_fk FOREIGN KEY (parentid)
      REFERENCES callrec.roles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT view_restriction_role_fk FOREIGN KEY (view_restrictionid)
      REFERENCES callrec.view_restrictions (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL
)
WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.roles TO GROUP callrecgrp;

ALTER TABLE callrec.roles OWNER TO postgres;
COMMENT ON TABLE callrec.roles IS 'Roles are groups of agents. Each role can contain other roles- tree structure.';
-- }}}

-- Make sure there can be only one row with NULL parentid
-- using a "trick" with UNIQUE INDEX on a constant.
CREATE UNIQUE INDEX _roles_parentid_null_uk
  ON callrec.roles ((1)) WHERE parentid IS NULL;
