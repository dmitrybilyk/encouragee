ALTER DOMAIN callrec.delete_mark DROP CONSTRAINT dom_delete_mark;

-- D - deleted
-- F - failed
-- S - segment delete request
-- M - media delete request
ALTER DOMAIN callrec.delete_mark ADD CONSTRAINT dom_delete_mark CHECK (VALUE::text = ANY (ARRAY['D'::character varying, 'F'::character varying, 'S'::character varying, 'M'::character varying]::text[]));
