
-- Index: _couple_extdata_lower_cplid_col_1_idx

-- DROP INDEX _couple_extdata_lower_cplid_col_1_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_1_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_1) text_pattern_ops, cplid)
  WHERE lower(col_1) IS NOT NULL;

-- Index: _couple_extdata_lower_cplid_col_2_idx

-- DROP INDEX _couple_extdata_lower_cplid_col_2_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_2_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_2) text_pattern_ops, cplid)
  WHERE lower(col_2) IS NOT NULL;

-- Index: _couple_extdata_lower_cplid_col_3_idx

-- DROP INDEX _couple_extdata_lower_cplid_col_3_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_3_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_3) text_pattern_ops, cplid)
  WHERE lower(col_3) IS NOT NULL;

-- Index: _couple_extdata_lower_cplid_col_4_idx

-- DROP INDEX _couple_extdata_lower_cplid_col_4_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_4_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_4) text_pattern_ops, cplid)
  WHERE lower(col_4) IS NOT NULL;

-- DROP INDEX _couple_extdata_lower_cplid_col_5_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_5_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_5) text_pattern_ops, cplid)
  WHERE lower(col_5) IS NOT NULL;

-- DROP INDEX _couple_extdata_lower_cplid_col_6_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_6_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_6) text_pattern_ops, cplid)
  WHERE lower(col_6) IS NOT NULL;

-- DROP INDEX _couple_extdata_lower_cplid_col_7_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_7_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_7) text_pattern_ops, cplid)
  WHERE lower(col_7) IS NOT NULL;

-- DROP INDEX _couple_extdata_lower_cplid_col_8_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_8_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_8) text_pattern_ops, cplid)
  WHERE lower(col_8) IS NOT NULL;

-- DROP INDEX _couple_extdata_lower_cplid_col_9_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_9_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_9) text_pattern_ops, cplid)
  WHERE lower(col_9) IS NOT NULL;


-- DROP INDEX _couple_extdata_lower_cplid_col_10_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_10_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_10) text_pattern_ops, cplid)
  WHERE lower(col_10) IS NOT NULL;


-- DROP INDEX _couple_extdata_lower_cplid_col_11_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_11_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_11) text_pattern_ops, cplid)
  WHERE lower(col_11) IS NOT NULL;

-- Index: _couple_extdata_lower_cplid_col_12_idx

-- DROP INDEX _couple_extdata_lower_cplid_col_12_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_12_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_12) text_pattern_ops, cplid)
  WHERE lower(col_12) IS NOT NULL;

-- Index: _couple_extdata_lower_cplid_col_13_idx

-- DROP INDEX _couple_extdata_lower_cplid_col_13_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_13_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_13) text_pattern_ops, cplid)
  WHERE lower(col_13) IS NOT NULL;

-- Index: _couple_extdata_lower_cplid_col_14_idx

-- DROP INDEX _couple_extdata_lower_cplid_col_14_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_14_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_14) text_pattern_ops, cplid)
  WHERE lower(col_14) IS NOT NULL;

-- DROP INDEX _couple_extdata_lower_cplid_col_15_idx;

CREATE INDEX _couple_extdata_lower_cplid_col_15_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_15) text_pattern_ops, cplid)
  WHERE lower(col_15) IS NOT NULL;


