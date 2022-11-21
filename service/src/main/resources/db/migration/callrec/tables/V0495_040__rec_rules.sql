-->>
-- <code>userid</code>: creator of this rule (informational),
-- inserted values are not examined in any way
--
-- <code>roleid</code>: role that this rule belongs to
--
-- <code>isactive</code>: rules can be temporarily disabled
--
-- <code>weekdays</code> specifies on which days this rule is active
--   1 = Mon
--   2 = Tue
--   4 = Wed
--   8 = Thu
--  16 = Fri
--  32 = Sat
--  64 = Sun
--
-- <code>starttime</code>, <code>stoptime</code>: seconds since midnight
--
-- <code>numbermask</code>: telephone numbers this rule applies to
--
-- <code>probability</code>: probability in percents this rule will be
-- applied to a call
--<<
CREATE TABLE callrec.rec_rules (
  id INTEGER NOT NULL,
  userid INTEGER DEFAULT 1,
  roleid INTEGER,
  isactive BOOLEAN,
  weekdays BIT(7),
  starttime INTEGER,
  stoptime INTEGER,
  numbermask VARCHAR(255) DEFAULT '',
  priority INTEGER,
  probability INTEGER,
  policy callrec.rec_rules_policy,
  factory VARCHAR(32) DEFAULT '',
  CONSTRAINT rec_rules_pk PRIMARY KEY (id),
  CONSTRAINT rec_rules_userid_fk
    FOREIGN KEY (userid) REFERENCES callrec.users (id) ON DELETE SET DEFAULT,
  CONSTRAINT rec_rules_roleid_fk
    FOREIGN KEY (roleid) REFERENCES callrec.roles (id) ON DELETE CASCADE,
  CONSTRAINT valid_priority CHECK (priority >= 0 AND priority < 1000)
) WITHOUT OIDS;

GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.rec_rules TO GROUP callrecgrp;

COMMENT ON TABLE callrec.rec_rules IS 'Recording rules, all options. Screen rec can have more info in rec_rules_process.';
COMMENT ON COLUMN callrec.rec_rules.policy IS 'RECORD,PRE_RECORD,NO_RECORD,IGNORE, see domain rec_rules_policy';

