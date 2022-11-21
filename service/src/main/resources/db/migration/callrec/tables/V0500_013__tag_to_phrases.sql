CREATE TABLE callrec.tag_to_phrases
(
   phraseid integer,
   tagid integer,

   CONSTRAINT tag_to_phrases_pk PRIMARY KEY (tagid, phraseid),
   CONSTRAINT tag_to_phrases_tagid_fk FOREIGN KEY (tagid) REFERENCES callrec.speech_tags (tagid)    ON UPDATE CASCADE ON DELETE CASCADE,
   CONSTRAINT tag_to_phrases_phraseid_fk FOREIGN KEY (phraseid) REFERENCES callrec.speech_phrases (phraseid)    ON UPDATE CASCADE ON DELETE CASCADE
) WITH (OIDS=FALSE)
;
ALTER TABLE callrec.tag_to_phrases OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.tag_to_phrases TO callrecgrp;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.tag_to_phrases TO wbscgrp;
