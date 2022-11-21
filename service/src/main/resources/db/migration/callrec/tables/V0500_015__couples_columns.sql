alter table callrec.couples add column direction VARCHAR(25) default 'UNKNOWN';

alter table callrec.couples add CONSTRAINT couples_direction CHECK (direction in ('INCOMING', 'OUTGOING','INTERNAL', 'UNKNOWN'));

-- CAL-5553
alter table callrec.couples
  add column callingagent VARCHAR(255),
  add column calledagent VARCHAR(255);


-- CAL-5541
alter table callrec.couples
  add column calling_domain VARCHAR(255),
  add column called_domain VARCHAR(255),
  add column original_domain VARCHAR(255);


-- CAL-5553
ALTER TABLE callrec.couples
  add column dayofweek INTEGER CONSTRAINT valid_day_of_week CHECK ((dayofweek >= 0) AND (dayofweek <= 6)), -- values from 0..6
  add column timeofday INTEGER CONSTRAINT positive_time CHECK (timeofday >= 0); -- numeric positive values


-- CAL-5708
ALTER DOMAIN callrec.couple_state DROP CONSTRAINT dom_couple_state;
ALTER DOMAIN callrec.couple_state ADD CONSTRAINT dom_couple_state CHECK (value IN ('EXT_DATA_SAVED', 'DECODED', 'FINISHED'));

