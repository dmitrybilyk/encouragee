-- CAL-17703 extend max number of advanced search fields to 30

-- Indices for new columns in couple_fixed_extdata

CREATE INDEX _couple_extdata_lower_cplid_col_16_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_16) text_pattern_ops, cplid)
  WHERE lower(col_16) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_17_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_17) text_pattern_ops, cplid)
  WHERE lower(col_17) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_18_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_18) text_pattern_ops, cplid)
  WHERE lower(col_18) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_19_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_19) text_pattern_ops, cplid)
  WHERE lower(col_19) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_20_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_20) text_pattern_ops, cplid)
  WHERE lower(col_20) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_21_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_21) text_pattern_ops, cplid)
  WHERE lower(col_21) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_22_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_22) text_pattern_ops, cplid)
  WHERE lower(col_22) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_23_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_23) text_pattern_ops, cplid)
  WHERE lower(col_23) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_24_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_24) text_pattern_ops, cplid)
  WHERE lower(col_24) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_25_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_25) text_pattern_ops, cplid)
  WHERE lower(col_25) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_26_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_26) text_pattern_ops, cplid)
  WHERE lower(col_26) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_27_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_27) text_pattern_ops, cplid)
  WHERE lower(col_27) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_28_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_28) text_pattern_ops, cplid)
  WHERE lower(col_28) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_29_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_29) text_pattern_ops, cplid)
  WHERE lower(col_29) IS NOT NULL;

CREATE INDEX _couple_extdata_lower_cplid_col_30_idx
  ON callrec.couple_fixed_extdata
  USING btree
  (lower(col_30) text_pattern_ops, cplid)
  WHERE lower(col_30) IS NOT NULL;
