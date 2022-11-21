-- CREATE TABLE calls {{{
-->>
-- A call is a set of related couples: a simple, no-bells-or-whisles call,
-- a conference, or a transferred call.
--
-- <code>cplcnt</code>: Number of couples associated with this call,
-- maintained by the application
--
-- <code>realcplcnt</code>: Number of couples associated with this call,
-- maintained by the database
--
-- SEE ALSO: views/calls.sql, rules/calls_delete.sql, rules/calls_insert.sql,
-- rules/calls_update.sql, tables/couples.sql, views/couples.sql,
-- functions/add_call.sql, functions/get_next_callid.sql,
-- functions/get_curr_callid.sql
--<<

CREATE TABLE callrec.calls (
  -- see GET_NEXT_CALLID()
  id INTEGER NOT NULL,
  -- number of this call's stored couples (maintained by callrec)
  cplcnt INTEGER,
  start_ts  TIMESTAMP WITH TIME ZONE,
  stop_ts  TIMESTAMP WITH TIME ZONE,
  "length" INTEGER DEFAULT 0,
  -- number of this call's stored couples (maintained by database)
  realcplcnt INTEGER,
  CONSTRAINT _calls_pk PRIMARY KEY (id)
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.calls TO GROUP callrecgrp;

COMMENT ON TABLE callrec.calls IS 'Information about call which may consist of more couples.
 Start and stop is counted as min resp. max from all couples start resp. stop  which belong to this call.';
COMMENT ON COLUMN callrec.calls.cplcnt IS 'How many couples was originaly created to this call';
COMMENT ON COLUMN callrec.calls.realcplcnt IS 'Number of couples that belongs to this call now (Some might be deleted).';
-- }}}

