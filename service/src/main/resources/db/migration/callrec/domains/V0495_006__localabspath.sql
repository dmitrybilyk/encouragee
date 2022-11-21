-- CREATE DOMAIN localabspath {{{
CREATE DOMAIN callrec.localabspath AS VARCHAR
  CONSTRAINT dom_localabspath CHECK (
    VALUE IS NULL OR callrec.IS_LOCALABSPATH(VALUE)
  );
-- }}}

