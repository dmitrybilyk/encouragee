CREATE SEQUENCE callrec.voice_tags_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE callrec.voice_tags_seq OWNER TO postgres;
GRANT SELECT, UPDATE ON TABLE callrec.voice_tags_seq TO wbscgrp;
GRANT SELECT, USAGE ON TABLE callrec.voice_tags_seq TO wbsc;
GRANT SELECT, USAGE ON TABLE callrec.voice_tags_seq TO callrec;

CREATE TABLE callrec.voice_tags
(
   voice_tagid integer NOT NULL DEFAULT nextval('callrec.voice_tags_seq'::regclass),
   cfileid integer NOT NULL ,
   phraseid integer NOT NULL,
   from_time INTEGER NOT NULL check (from_time >= 0),
   to_time INTEGER NOT NULL check (to_time >= from_time),
   confidence float check (confidence>=0 or confidence <= 100),
   channel character varying(1) check (channel = 'L' or channel = 'R'),

   CONSTRAINT voice_tags_pk PRIMARY KEY (voice_tagid),
   CONSTRAINT voice_tags_cfiles_fk FOREIGN KEY (cfileid) REFERENCES callrec.cfiles (id)    ON UPDATE CASCADE ON DELETE CASCADE,
   CONSTRAINT voice_tags_phrases_fk FOREIGN KEY (phraseid) REFERENCES callrec.speech_phrases (phraseid)    ON UPDATE CASCADE ON DELETE CASCADE
) WITH (OIDS=FALSE)
;
ALTER TABLE callrec.voice_tags OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.voice_tags TO callrecgrp;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.voice_tags TO wbscgrp;
COMMENT ON TABLE callrec.voice_tags IS 'Voice tags refers to one audio file and one phrase. It holds information about phrase accuracy in concrete time range and channel (left/right speaker)';

CREATE INDEX voice_phrase
  ON callrec.voice_tags
  USING btree
  (cfileid , phraseid );
CREATE INDEX "phraseIndex"
  ON callrec.voice_tags
  USING btree
  (phraseid , confidence );
