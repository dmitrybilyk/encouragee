-- CREATE DOMAIN emailaddrspec {{{
CREATE DOMAIN callrec.emailaddrspec AS VARCHAR
  CONSTRAINT dom_emailaddrspec CHECK (
       VALUE = ''
    OR callrec.IS_EMAILADDRESS(VALUE)
  );
-- }}}

