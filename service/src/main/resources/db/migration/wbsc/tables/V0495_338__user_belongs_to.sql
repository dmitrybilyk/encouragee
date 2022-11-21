-- Table: wbsc.user_belongsto_ccgroup

-- DROP TABLE wbsc.user_belongsto_ccgroup;

CREATE TABLE wbsc.user_belongsto_ccgroup
(
  userid integer NOT NULL,
  ccgroupid integer NOT NULL,
  CONSTRAINT user_belongsto_ccgroup_pkey PRIMARY KEY (ccgroupid, userid),
  CONSTRAINT ccgroup_fk FOREIGN KEY (ccgroupid)
      REFERENCES wbsc.ccgroups (ccgroupid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT user_fk FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.user_belongsto_ccgroup OWNER TO postgres;
COMMENT ON TABLE wbsc.user_belongsto_ccgroup IS 'M:N table';

GRANT select, update, insert, delete on wbsc.user_belongsto_ccgroup to wbscgrp;
