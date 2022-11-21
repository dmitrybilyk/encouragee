-- Table: wbsc.messagebox

-- DROP TABLE wbsc.messagebox;

CREATE TABLE wbsc.messagebox
(
  messageboxid integer NOT NULL DEFAULT nextval('wbsc.seq_messagebox'::regclass),
  to_user integer NOT NULL,
  datetime timestamp with time zone NOT NULL,
  subject character varying(255) NOT NULL,
  from_user integer NOT NULL,
  carbon_copy integer,
  "text" text,
  to_status wbsc.messagebox_status_domain NOT NULL DEFAULT 'NEW',
  from_status wbsc.messagebox_status_domain NOT NULL DEFAULT 'NEW',
  cc_status wbsc.messagebox_status_domain NOT NULL DEFAULT 'NEW',
  CONSTRAINT messagebox_pkey PRIMARY KEY (messageboxid),
  CONSTRAINT messagebox_from_fk FOREIGN KEY (from_user)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT messagebox_to_fk FOREIGN KEY (to_user)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.messagebox OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.messagebox to wbscgrp;
