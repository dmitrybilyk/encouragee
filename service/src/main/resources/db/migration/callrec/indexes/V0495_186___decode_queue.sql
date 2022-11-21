-- $Id: decode_queue.sql,v 1.1 2007-03-29 12:30:24 karl Exp $

CREATE INDEX _decode_queue_sgid_idx ON callrec.decode_queue (sgid);
