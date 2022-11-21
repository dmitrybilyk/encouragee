CREATE INDEX _call_startts_idx ON callrec.calls   USING btree (start_ts);
CREATE INDEX _call_stopts_idx ON callrec.calls   USING btree (stop_ts);
CREATE UNIQUE INDEX _call_start_id_idx  ON callrec.calls  USING btree  (start_ts, id);