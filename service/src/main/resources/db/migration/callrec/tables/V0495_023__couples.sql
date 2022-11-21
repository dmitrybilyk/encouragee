-- CREATE TABLE couples {{{
-->>
-- A couple is an association between two endpoints, more commonly called
-- call leg or dialog (SIP).
--
-- A couple has zero or more cfiles associated with it.
--
-- <code>start_ts</code>, <code>stop_ts</code>: time of start and end of the
-- couple as UNIX timestamps (seconds since Epoch)
--
-- <code>created_ts</code>: application-defined point in life of the recording,
-- as a UNIX timestamp
--
-- <code>start_ts</code>, <code>stop_ts</code>, <code>created_ts</code>:
-- same as above, in the form of the SQL TIMESTAMP WITH TIME ZONE type.
--
--
-- Note that this value needs to be recomputed for all tuples in
-- <code>couples c</code> every time a <code>NEW</code> tuple is inserted with
-- <code>start_ts</code>, <code>stop_ts</code> interval such that
--
--  (c.start_ts, c.start_ts) OVERLAPS (NEW.start_ts, NEW.stop_ts)
--
-- since the original computation operates (obviously) only on data already
-- present in the table.
--
-- <code>cfcnt</code>: number of records in cfiles which reference
-- this couple (IOW, this will always return 0 rows:
--  SELECT id FROM couples cpl WHERE cfcnt != (
--    SELECT COUNT(*) FROM cfiles cf WHERE cplid = cpl.id
--  );
--
-- <code>cftypes</code>: types of files associated with this couple,
-- as a bitfield:
--
--   1 = AUDIO
--   2 = MAG
--   4 = ARCHIVE
--   8 = IMAGE
--  16 = VIDEO
--  32 = RECD
--
-- SEE ALSO: views/couples.sql, rules/couples_delete.sql, rules/couples_insert.sql,
-- rules/couples_update.sql, functions/add_couple.sql,
-- functions/del_couple.sql, functions/get_next_coupleid.sql,
-- functions/get_curr_coupleid.sql, functions/has_cftype.sql
--<<

CREATE TABLE callrec.couples (
  -- see GET_NEXT_COUPLEID()
  id INTEGER NOT NULL,
  -- this couple's call (FK)
  callid INTEGER NOT NULL,
  -- start, stop, creation times as sql timestamps
  start_ts TIMESTAMP WITH TIME ZONE not null,
  stop_ts TIMESTAMP WITH TIME ZONE not null,
  created_ts TIMESTAMP WITH TIME ZONE,
  "length" INTEGER DEFAULT 0,
  -- number of this couple's stored cfiles (maintained by database)
  cfcnt INTEGER,
  -- types of this couple's cfiles, as a 4-byte bitfield
  cftypes BIT(32),
  callingip INET,
  calledip INET,
  callingnr VARCHAR(255),
  originalcallednr VARCHAR(255),
  finalcallednr VARCHAR(255),
  callingpartyname VARCHAR(255),
  calledpartyname VARCHAR(255),
  -- couple type
  cpltype callrec.cpltype DEFAULT 'NORMAL',
  problemstatus callrec.problemstatus DEFAULT 'NO_PROBLEM',
  description TEXT DEFAULT '',
  protected BOOLEAN,
  -- couple sid
  sid VARCHAR(255) NOT NULL,
  -- backup / archival info
  synchronized callrec.synchro_mark,
  mixed callrec.mixer_mark,
  deleted callrec.delete_mark,
  restored callrec.restore_mark,
  archived callrec.archive_mark,
  evaluated callrec.evaluate_mark,
  b_method callrec.b_method,
  b_location VARCHAR(255),
  state callrec.couple_state,
  CONSTRAINT _couples_pk PRIMARY KEY (id),
  CONSTRAINT _couples_sid_unique UNIQUE(sid),
  CONSTRAINT _couples_callid_fk
    FOREIGN KEY (callid) REFERENCES callrec.calls(id) ON DELETE CASCADE
) WITHOUT OIDS;

ALTER TABLE callrec.couples OWNER TO postgres;
GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.couples TO GROUP callrecgrp;

COMMENT ON TABLE callrec.couples IS 'Basic information about couple are hold here. To wich call this couple belongs, dates, numbers and status of couple.';
COMMENT ON COLUMN callrec.couples.cfcnt IS 'Cfiles count. How many files belongs to this couple.';
COMMENT ON COLUMN callrec.couples.cftypes IS 'This collumn represent all cfile types in bit representation. see function callrec.GET_CFTYPEMASK()';
COMMENT ON COLUMN callrec.couples.cpltype IS 'Type of call - e.g. conference, park, unpark etc. See cpltype domain.';
COMMENT ON COLUMN callrec.couples.problemstatus IS 'Status of couple - NO_PROBLEM, NO_STREAM etc. See problemstatus domain.';
COMMENT ON COLUMN callrec.couples.protected IS 'Couple is protected against deleting is set to true. Trigger trig_couples_before_delete and trig_cfiles_before_delete checks this.';
COMMENT ON COLUMN callrec.couples.sid IS 'Synchronization id which can be generated based on call info. Is same in all databases for same call. Is used by synchro tool.';
COMMENT ON COLUMN callrec.couples.b_method IS 'Method of archivation, usually ARC';
COMMENT ON COLUMN callrec.couples.b_location IS 'Where the arcihve is stored - path.';
-- }}}

