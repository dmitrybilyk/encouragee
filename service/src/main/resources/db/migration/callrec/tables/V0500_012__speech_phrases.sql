CREATE SEQUENCE callrec.speech_phrases_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE callrec.speech_phrases_seq OWNER TO postgres;
GRANT SELECT, UPDATE ON TABLE callrec.speech_phrases_seq TO wbscgrp;
GRANT SELECT, USAGE ON TABLE callrec.speech_phrases_seq TO wbsc;
GRANT SELECT, USAGE ON TABLE callrec.speech_phrases_seq TO callrec;

CREATE TABLE callrec.speech_phrases
(
phraseid integer NOT NULL DEFAULT nextval('callrec.speech_phrases_seq'::regclass),
phrase_text text NOT NULL,
confidence float check (confidence>=0 or confidence <= 100),
interaction_count integer,
last_count_date timestamp with time zone,
state character varying(255) check (state = 'READY' or state = 'INDEXING' or state = 'UPDATING'),
enabled boolean,
CONSTRAINT _phrases_text_unique UNIQUE(phrase_text),
CONSTRAINT speech_phrases_pk PRIMARY KEY (phraseid)
) WITH (OIDS=FALSE)
;
ALTER TABLE callrec.speech_phrases OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.speech_phrases TO callrecgrp;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.speech_phrases TO wbscgrp;
COMMENT ON TABLE callrec.speech_phrases IS ' Phrase is word or several words which we want to be indexed. These words are stored in column phrase_text. Has three possible states- INDEXING, READY, UPDATING';

