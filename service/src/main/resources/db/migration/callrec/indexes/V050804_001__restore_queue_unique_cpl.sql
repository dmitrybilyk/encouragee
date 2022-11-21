-- drop previous invalid index (unique call ID + couple ID)
ALTER TABLE callrec.restore_queue
  DROP CONSTRAINT IF EXISTS _restore_queue_unique_call_cpl;

-- create new correct index (unique couple ID)
ALTER TABLE callrec.restore_queue
  ADD CONSTRAINT restore_queue_unique_cpl UNIQUE (cplid);