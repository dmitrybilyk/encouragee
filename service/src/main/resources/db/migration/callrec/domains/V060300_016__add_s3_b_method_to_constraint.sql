ALTER DOMAIN callrec.b_method
DROP CONSTRAINT dom_b_method;

ALTER DOMAIN callrec.b_method
ADD CONSTRAINT dom_b_method CHECK (
VALUE IN ('TAPE', 'DISK_A', 'DISK_N', 'ARC', 'CEN', 'S3_STORAGE'));
