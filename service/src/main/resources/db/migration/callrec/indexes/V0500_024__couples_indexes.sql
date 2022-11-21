
CREATE INDEX couples_direction_idx
  ON callrec.couples
  USING btree
  (direction);

CREATE INDEX _couples_callingagent_idx ON callrec.couples USING btree (callingagent);
CREATE INDEX _couples_calledagent_idx ON callrec.couples USING btree (calledagent);

CREATE INDEX _couples_dayofweek_idx ON callrec.couples USING btree (dayofweek);
CREATE INDEX _couples_timeofday_idx ON callrec.couples USING btree (timeofday);

