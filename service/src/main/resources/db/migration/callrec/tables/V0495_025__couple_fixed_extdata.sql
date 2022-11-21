CREATE TABLE callrec.couple_fixed_extdata
(
  cplid integer NOT NULL,
  col_1 text,
  col_2 text,
  col_3 text,
  col_4 text,
  col_5 text,
  col_6 text,
  col_7 text,
  col_8 text,
  col_9 text,
  col_10 text,
  col_11 text,
  col_12 text,
  col_13 text,
  col_14 text,
  col_15 text,
  CONSTRAINT fixed_primary PRIMARY KEY (cplid),
  CONSTRAINT fixed_cplid FOREIGN KEY (cplid)
      REFERENCES callrec.couples (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE callrec.couple_fixed_extdata OWNER TO callrec;
GRANT ALL ON TABLE callrec.couple_fixed_extdata TO callrec;
GRANT ALL ON TABLE callrec.couple_fixed_extdata TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.couple_fixed_extdata TO postgres;
COMMENT ON TABLE callrec.couple_fixed_extdata IS 'Holds data which should be displayed with couple, user wants to sort the couples according to external data. Each column hold different key to cplid.';
