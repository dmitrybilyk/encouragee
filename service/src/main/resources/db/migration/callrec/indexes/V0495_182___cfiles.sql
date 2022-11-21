CREATE INDEX _cfiles_sgid_idx ON callrec.cfiles (sgid);
CREATE INDEX _cfiles_cplid_idx ON callrec.cfiles (cplid);
CREATE UNIQUE INDEX _cfiles_path_uk_like ON callrec.cfiles ((cfpath) varchar_pattern_ops);
CREATE INDEX _cfiles_cftype_idx ON callrec.cfiles (cftype);
