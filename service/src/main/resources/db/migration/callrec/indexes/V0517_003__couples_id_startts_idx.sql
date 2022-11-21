DROP INDEX IF EXISTS _couples_id_startts_idx;

CREATE INDEX _couples_id_startts_idx  ON callrec.couples  USING btree  (start_ts DESC, id);
