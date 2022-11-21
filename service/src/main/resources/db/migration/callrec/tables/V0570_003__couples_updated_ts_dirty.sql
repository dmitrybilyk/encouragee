ALTER TABLE callrec.couples ADD COLUMN updated_ts timestamp with time zone default null;
CREATE INDEX couples_updated_ts_idx ON callrec.couples (updated_ts);

ALTER TABLE callrec.couples ADD COLUMN dirty boolean NOT NULL DEFAULT true;
CREATE INDEX couples_dirty_idx ON callrec.couples (dirty DESC);