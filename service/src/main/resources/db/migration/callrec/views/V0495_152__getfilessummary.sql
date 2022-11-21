-- $Id: getfilessummary.sql,v 1.4 2007-03-23 09:24:42 karl Exp $

CREATE OR REPLACE VIEW callrec.getfilessummary AS
 SELECT cp.stop_ts, cf.cfpath AS filepath
   FROM callrec.cfiles cf, callrec.couples cp
  WHERE cf.cplid = cp.id;

GRANT SELECT ON TABLE callrec.getfilessummary TO GROUP callrecgrp;
