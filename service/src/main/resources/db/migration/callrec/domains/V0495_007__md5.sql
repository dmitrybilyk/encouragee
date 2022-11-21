CREATE DOMAIN callrec.md5_d AS VARCHAR(32)
  CONSTRAINT dom_md5 CHECK (
       callrec.IS_MD5(VALUE)
    OR VALUE IS NULL
  );
