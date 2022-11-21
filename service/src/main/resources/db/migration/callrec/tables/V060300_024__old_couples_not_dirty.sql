DO $$
BEGIN
  IF NOT EXISTS(SELECT 1
                FROM callrec.couples
                WHERE updated_ts IS NOT NULL
                LIMIT 1)
  THEN
    EXECUTE 'alter table callrec.couples drop column dirty;';
    EXECUTE 'alter table callrec.couples add column dirty boolean;';
    EXECUTE 'alter table callrec.couples alter column dirty set default true;'; -- reason is to synchronize all till now recorded
    EXECUTE 'create index couples_dirty_idx on callrec.couples using btree (id, dirty) where dirty ;';
    EXECUTE 'create index couples_updated_ts_id_not_dirty_idx on callrec.couples using btree (updated_ts, id) where dirty = false;';
  END IF;

END;
$$
