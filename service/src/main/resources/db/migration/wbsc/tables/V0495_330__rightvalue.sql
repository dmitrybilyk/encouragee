-- Table: wbsc.rightvalue

-- DROP TABLE wbsc.rightvalue;

CREATE TABLE wbsc.rightvalue
(
  rightvalueid integer NOT NULL DEFAULT nextval('wbsc.seq_rightvalues'::regclass),
  "value" text,
  rightid integer NOT NULL,
  CONSTRAINT rightvalue_pkey PRIMARY KEY (rightvalueid),
  CONSTRAINT right_fk FOREIGN KEY (rightid)
      REFERENCES wbsc.rights (rightid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.rightvalue OWNER TO postgres;
COMMENT ON TABLE wbsc.rightvalue IS 'holds additional value for a right';

GRANT select, update, insert, delete on wbsc.rightvalue to wbscgrp;
