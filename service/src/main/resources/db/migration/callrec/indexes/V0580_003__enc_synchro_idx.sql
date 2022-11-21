DROP INDEX callrec.couples_updated_ts_idx;

CREATE INDEX couples_updated_ts_id_not_dirty_idx
ON callrec.couples
USING btree
(updated_ts ASC, id asc)
where dirty = false;
