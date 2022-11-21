-- SC-5363
CREATE TABLE wbsc.smediafiles
(
  smediafileid integer NOT NULL DEFAULT nextval('wbsc.seq_smediafiles'::regclass),
  mftype character varying(8) NOT NULL,
  path character varying(255) NOT NULL,
  content_type character varying(255) NOT NULL,
  CONSTRAINT smediafiles_pkey PRIMARY KEY (smediafileid),
  CONSTRAINT smediafiles_mftype_ck CHECK (
    mftype IN (
      'AUDIO',
      'VIDEO',
      'IMAGE',
      'RECD'
    )
  ),
  CONSTRAINT smediafiles_path_uq UNIQUE (path)
)
WITHOUT OIDS;
ALTER TABLE wbsc.smediafiles OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.smediafiles to wbscgrp;
