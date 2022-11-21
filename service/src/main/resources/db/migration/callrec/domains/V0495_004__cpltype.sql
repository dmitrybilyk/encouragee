-- CREATE DOMAIN cpltype {{{
CREATE DOMAIN callrec.cpltype AS VARCHAR
  CONSTRAINT dom_cpltype CHECK (
    VALUE IN (
      'NORMAL',
      'CONF',
      'RECONN',
      'PARK',
      'UNPARK',
      'BARGE',
      'INSTREAMER'
    )
  );
-- }}}

