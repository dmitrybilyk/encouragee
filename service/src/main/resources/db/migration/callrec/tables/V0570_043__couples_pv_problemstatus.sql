-- CAL-14190 & CAL-14465
ALTER TABLE callrec.couples ADD COLUMN calling_pv_problemstatus callrec.problemstatus;
ALTER TABLE callrec.couples ADD COLUMN called_pv_problemstatus callrec.problemstatus;

COMMENT ON COLUMN callrec.couples.calling_pv_problemstatus IS
'Status of calling phone video (See problemstatus domain). Default is null == should not be recorded';
COMMENT ON COLUMN callrec.couples.called_pv_problemstatus IS
'Status of called phone video (See problemstatus domain). Default is null == should not be recorded';
