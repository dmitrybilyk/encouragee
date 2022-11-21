CREATE OR REPLACE VIEW callrec.vw_decode_queue AS
 SELECT cf.cplid, dq.dqid, dq.sgid, cf.id AS cfid, cf.cfpath
   FROM callrec.decode_queue dq
   JOIN callrec.cfiles cf USING (sgid);

ALTER TABLE callrec.vw_decode_queue OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE, REFERENCES, TRIGGER ON TABLE callrec.vw_decode_queue TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.vw_decode_queue TO callrecgrp;

CREATE OR REPLACE RULE decode_queue_delete AS
    ON DELETE TO callrec.vw_decode_queue DO INSTEAD  DELETE FROM callrec.decode_queue
WHERE decode_queue.dqid::text = old.dqid::text;
