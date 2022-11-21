CREATE DOMAIN callrec.cksum_t_typ_d AS VARCHAR
  CONSTRAINT dom_cksum_t_typ_d CHECK (
    VALUE IN (
      'CRC32',''
    )
  );

