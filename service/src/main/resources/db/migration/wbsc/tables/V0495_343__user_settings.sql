-- Table: wbsc.user_settings

-- DROP TABLE wbsc.user_settings;

CREATE TABLE wbsc.user_settings
(
  usersettingid integer NOT NULL DEFAULT nextval('wbsc.seq_user_settings'::regclass),
  userid integer NOT NULL,
  "key" character varying(255) NOT NULL,
  "value" text NOT NULL,
  "comment" text,
  CONSTRAINT user_settings_pkey PRIMARY KEY (usersettingid),
  CONSTRAINT user_settings_users_fk FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT user_settings_unique UNIQUE (key, userid)
)
WITHOUT OIDS;
ALTER TABLE wbsc.user_settings OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.user_settings to wbscgrp;
