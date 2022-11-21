-- change timestamp(0) to timestamp type
ALTER TABLE wbsc.sc_users
ALTER COLUMN delegation_from TYPE timestamp with time zone,
ALTER COLUMN delegation_to TYPE timestamp with time zone;