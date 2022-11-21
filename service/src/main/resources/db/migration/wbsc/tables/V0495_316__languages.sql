-- Table: wbsc.languages

-- DROP TABLE wbsc.languages;

CREATE TABLE wbsc.languages
(
  languageid integer NOT NULL DEFAULT nextval('wbsc.seq_languages'::regclass),
  display_name character varying(50) NOT NULL,
  "language" character varying(3) NOT NULL,
  country character varying(3) NOT NULL,
  main_language boolean NOT NULL DEFAULT false,
description text,
  CONSTRAINT languages_pkey PRIMARY KEY (languageid)
)
WITHOUT OIDS;
ALTER TABLE wbsc.languages OWNER TO postgres;

alter table wbsc.languages
add constraint languages_unique_name unique(display_name);

GRANT select, update, insert, delete on wbsc.languages to wbscgrp;
