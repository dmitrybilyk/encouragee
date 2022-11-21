-- $Id: couple_extdata.sql,v 1.4 2010-05-18 09:56:48 mensik Exp $

CREATE INDEX _couple_extdata_cplid_idx ON callrec.couple_extdata (cplid);
CREATE INDEX _couple_extdata_key_idx ON callrec.couple_extdata (key);

