-- $Id: gettotals.sql,v 1.9 2010-04-01 15:52:45 mensik Exp $

CREATE OR REPLACE VIEW callrec.gettotals AS
 SELECT cp.id, cp.start_ts, cp.stop_ts, cp.length, cp.problemstatus,
        COALESCE(cp.synchronized, ''::character varying) AS synchronized, CASE WHEN (cftypes & 2::bit(32))=2::bit(32) THEN 1 ELSE 0 END AS decoder_requirement,
        cp.callingip, cp.calledip, cp.callingnr, cp.originalcallednr, cp.finalcallednr
   FROM callrec.couples cp;

GRANT SELECT ON TABLE callrec.gettotals TO GROUP callrecgrp;
