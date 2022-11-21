-- CREATE DOMAIN b_method {{{
CREATE DOMAIN callrec.b_method AS VARCHAR
  CONSTRAINT dom_b_method CHECK (
    VALUE IN (
      'TAPE',
      'DISK_A',
      'DISK_N',
      'ARC',
      'CEN'
    )
  );
-- }}}

