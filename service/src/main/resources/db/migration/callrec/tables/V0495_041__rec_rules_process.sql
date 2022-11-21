-- $Id: rec_rules_process.sql,v 1.8 2010-04-16 12:18:18 mensik Exp $

CREATE TABLE callrec.rec_rules_process (
    ruleid INTEGER NOT NULL,
    "type" VARCHAR(255),
    value TEXT,
    CONSTRAINT rec_rules_process_fk
      FOREIGN KEY (ruleid) REFERENCES callrec.rec_rules (id) ON DELETE CASCADE,
      CONSTRAINT _rec_rules_process_pk PRIMARY KEY (ruleid, "value")
) WITHOUT OIDS;

ALTER TABLE callrec.rec_rules_process OWNER TO postgres;
GRANT ALL ON TABLE callrec.rec_rules_process TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.rec_rules_process TO callrecgrp;
COMMENT ON TABLE callrec.rec_rules_process IS 'Additional information to recording rule, screenRec uses this.';
