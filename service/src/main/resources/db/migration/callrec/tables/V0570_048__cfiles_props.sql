-- CAL-14347 & CAL-14465
ALTER TABLE callrec.cfiles ADD COLUMN props VARCHAR(4000);
COMMENT ON COLUMN callrec.cfiles.props is 'Media properties JSON';

ALTER TABLE callrec.cfiles ADD COLUMN direction VARCHAR(7)
CONSTRAINT _cfiles_direction_ck CHECK (direction::text = ANY (ARRAY['CALLING'::character varying, 'CALLED'::character varying]::text[]));

COMMENT ON COLUMN cfiles.direction IS 'CALLING/CALLED flag';
