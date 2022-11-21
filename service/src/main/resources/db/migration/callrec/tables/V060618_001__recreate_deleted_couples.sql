CREATE TABLE callrec.deleted_couples(
  deleted_coupleid SERIAL,
  sid varchar(255) NOT NULL,
  deleted_ts timestamp with time zone default null,
  CONSTRAINT deleted_coupleid_pk PRIMARY KEY (deleted_coupleid)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE callrec.deleted_couples OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.deleted_couples TO callrecgrp;

CREATE INDEX deleted_coupleid_deleted_ts_idx ON callrec.deleted_couples (deleted_ts);
