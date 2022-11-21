-- SC-4139
CREATE INDEX sc_evalcalls_sid_idx
  ON wbsc.evalcalls
  USING btree
  (couplesid);