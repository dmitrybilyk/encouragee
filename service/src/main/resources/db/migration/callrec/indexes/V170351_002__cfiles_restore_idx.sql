CREATE INDEX IF NOT EXISTS _cfiles_restore_idx ON callrec.cfiles USING btree (cftype, cktype, ckvalue, cfsize, cfpath);
