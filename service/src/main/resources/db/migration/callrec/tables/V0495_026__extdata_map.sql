
CREATE TABLE callrec.extdata_map
(
  columnid smallint NOT NULL,
  "key" text,
  CONSTRAINT extdata_map_pk PRIMARY KEY (columnid),
  CONSTRAINT extdata_map_uq UNIQUE (key),
  CONSTRAINT _extdata_map_check CHECK (columnid > 0 AND columnid < 16)
)
WITHOUT OIDS;
ALTER TABLE callrec.extdata_map OWNER TO postgres;
GRANT ALL ON TABLE callrec.extdata_map TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.extdata_map TO callrecgrp;
