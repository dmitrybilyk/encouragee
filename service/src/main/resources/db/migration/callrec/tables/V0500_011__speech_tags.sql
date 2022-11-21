CREATE SEQUENCE callrec.speech_tags_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE callrec.speech_tags_seq OWNER TO postgres;
GRANT SELECT, UPDATE ON TABLE callrec.speech_tags_seq TO wbscgrp;
GRANT SELECT, USAGE ON TABLE callrec.speech_tags_seq TO wbsc;
GRANT SELECT, USAGE ON TABLE callrec.speech_tags_seq TO callrec;

CREATE TABLE callrec.speech_tags
(
tagid integer NOT NULL DEFAULT nextval('callrec.speech_tags_seq'::regclass),
tag_name character varying(255) NOT NULL,
state character varying(255) check (state = 'READY' or state = 'INDEXING' or state = 'UPDATING'),
searchid integer,-- saved search id - add foreign key!!!
icon character varying(255),
CONSTRAINT speech_tags_pk PRIMARY KEY (tagid)
) WITH (OIDS=FALSE)
;
ALTER TABLE callrec.speech_tags OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.speech_tags TO callrecgrp;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.speech_tags TO wbscgrp;
COMMENT ON TABLE callrec.speech_tags IS 'Speech tag contain one or more speech phrases (join on tag_to_phrases M:N). Tag represents general group of phrases like "All greetings".
 Then it has associated icon, search id from saved searches which this phrase should be applied on. Has three possible states- INDEXING, READY, UPDATING';
CREATE UNIQUE INDEX speech_tags_name_unq_idx on callrec.speech_tags (lower(tag_name));
