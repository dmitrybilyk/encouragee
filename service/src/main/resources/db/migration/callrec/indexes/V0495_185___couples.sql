CREATE INDEX _couples_stop_ts_idx ON callrec.couples (stop_ts);
CREATE INDEX _couples_callid_startts_idx ON callrec.couples (callid, start_ts);
CREATE INDEX _couples_caller_origcallee_idx ON callrec.couples (callingnr varchar_pattern_ops, originalcallednr varchar_pattern_ops);
CREATE INDEX _couples_origcallee_idx ON callrec.couples (originalcallednr varchar_pattern_ops);
CREATE INDEX _couples_length_idx ON callrec.couples (length);
CREATE INDEX _couples_cpltype_idx ON callrec.couples (cpltype);

CREATE INDEX _couples_description_idx  ON callrec.couples   USING btree  (description text_pattern_ops);
CREATE INDEX _couples_description_lower_idx   ON callrec.couples  USING btree  (lower (description) text_pattern_ops);

CREATE INDEX _couples_sid_state_idx   ON callrec.couples  USING btree   (sid,state);
CREATE INDEX _couples_stop_ts_synchronized_idx  ON callrec.couples  USING btree  (stop_ts,synchronized);
CREATE INDEX _couples_id_startts_idx  ON callrec.couples  USING btree  (start_ts DESC, id);

CREATE INDEX _couples_mixed_idx ON callrec.couples (mixed);
CREATE INDEX _couples_deleted_idx ON callrec.couples (deleted);
CREATE INDEX _couples_restored_idx ON callrec.couples (restored);
CREATE INDEX _couples_archived_idx ON callrec.couples (archived);
CREATE INDEX _couples_evaluated_idx ON callrec.couples (evaluated);



