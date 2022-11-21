CREATE TABLE callrec.calls_marker
(
  call_markerid SERIAL,
  callid integer NOT NULL,
  target VARCHAR(255) NOT NULL,
  CONSTRAINT call_marker_pk PRIMARY KEY (call_markerid),
  CONSTRAINT calls_marker_fk FOREIGN KEY (callid)
      REFERENCES calls (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE callrec.calls_marker   OWNER TO postgres;
GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.calls_marker TO GROUP callrecgrp;
