-- Table: wbsc.user_canevaluate_ccgroup

-- DROP TABLE wbsc.user_canevaluate_ccgroup;

CREATE TABLE wbsc.user_canevaluate_ccgroup
(
  userid integer NOT NULL,
  ccgroupid integer NOT NULL,
  CONSTRAINT user_canevaluate_ccgroup_pkey PRIMARY KEY (userid, ccgroupid),
  CONSTRAINT ccgroup_fk FOREIGN KEY (ccgroupid)
      REFERENCES wbsc.ccgroups (ccgroupid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT user_fk FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.user_canevaluate_ccgroup OWNER TO postgres;
COMMENT ON TABLE wbsc.user_canevaluate_ccgroup IS 'M:N table';

GRANT select, update, insert, delete on wbsc.user_canevaluate_ccgroup to wbscgrp;
