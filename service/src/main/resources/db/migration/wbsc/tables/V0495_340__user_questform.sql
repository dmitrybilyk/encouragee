-- Table: wbsc.user_questform

-- DROP TABLE wbsc.user_questform;

CREATE TABLE wbsc.user_questform
(
  userid integer NOT NULL,
  qformid integer NOT NULL,
  CONSTRAINT user_questform_pkey PRIMARY KEY (userid, qformid),
  CONSTRAINT questform_fk FOREIGN KEY (qformid)
      REFERENCES wbsc.questforms (qformid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT user_fk FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.user_questform OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.user_questform to wbscgrp;
