CREATE INDEX _call_extdata_cplid_idx ON callrec.call_extdata (callid);
CREATE INDEX _call_extdata_key_idx ON callrec.call_extdata (key);
CREATE INDEX _call_extdata_key_value_idx ON callrec.call_extdata (key, value text_pattern_ops);