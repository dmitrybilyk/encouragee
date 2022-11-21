DROP INDEX IF EXISTS _couples_id_startts_idx;
DROP INDEX IF EXISTS _couples_startts_idx;

CREATE INDEX  _couples_startts_idx
  ON callrec.couples
  USING btree
  (start_ts DESC) ;