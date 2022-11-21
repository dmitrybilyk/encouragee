CREATE INDEX IF NOT EXISTS _couples_startts_id_idx ON callrec.couples USING btree (start_ts,id);
