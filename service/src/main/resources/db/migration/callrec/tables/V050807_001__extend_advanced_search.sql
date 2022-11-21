-- CAL-17703 extend max number of advanced search fields to 30

-- New columns in couple_fixed_extdata

ALTER TABLE couple_fixed_extdata ADD COLUMN col_16 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_17 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_18 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_19 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_20 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_21 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_22 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_23 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_24 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_25 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_26 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_27 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_28 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_29 text;
ALTER TABLE couple_fixed_extdata ADD COLUMN col_30 text;


-- Extdata map
  
ALTER TABLE extdata_map DROP CONSTRAINT _extdata_map_check;
ALTER TABLE extdata_map ADD CONSTRAINT _extdata_map_check CHECK (columnid > 0 AND columnid < 31);
