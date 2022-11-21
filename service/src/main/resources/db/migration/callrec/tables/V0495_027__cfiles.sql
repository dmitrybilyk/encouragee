-- CREATE TABLE cfiles {{{
-->>
-- A cfile represents a file containing voice or other media data. A couple
-- representing two audio streams will have to cfiles until they're
-- converted into a single mp3 file.
--
-- <code>sgid</code>: cfiles are organized into streamgroups, e. g. the two
-- streams of an audio-only dialog form a streamgroup.
--
-- <code>cplid</code>: foreign key into couples.id
--
-- <code>props</code> is a row-typed value
--
-- <code>cf_type</code>: for list of accepted values
-- SEE domains/cf_type.sql
--
-- <code>props.cksum</code> is a row-typed value:
--
-- <code>props.cksum.cktype</code>: for list of accepted values SEE
-- domains/cksum_t_typ_d.sql
--
-- <code>props.cksum.value</code>: checksum value.
--
-- <code>props.bytes</code>: size of the file in bytes
--
-- <code>props.path</code>: absolute path to the file
--
-- SEE ALSO: domains/cf_type.sql, domains/localabspath.sql,
-- functions/get_next_sgrpid.sql,
-- functions/is_localabspath.sql, functions/add_cfile.sql,
-- functions/get_next_cfileid.sql,
-- functions/add_streamgroup.sql,
-- functions/decode_streamgroup.sql, functions/del_cfile.sql,
-- triggers/trigdel_cfiles.sql, functions/trig_cfiles_after_delete.sql,
-- triggers/trigins_cfiles.sql, functions/trig_cfiles_after_insert.sql,
-- domains/cf_type.sql,
-- domains/cksum_t_typ_d.sql
--<<

CREATE TABLE callrec.cfiles (
  -- this cfile's couple
  id INTEGER NOT NULL,
  sgid INTEGER NOT NULL,
  cplid INTEGER NOT NULL,
  cftype VARCHAR(8),
  cktype callrec.cksum_t_typ_d,
  ckvalue bigint,
  cfsize INTEGER NOT NULL,
  cfpath callrec.localabspath,
  enc_key_id VARCHAR,
  digest VARCHAR,
  start_ts TIMESTAMP WITH TIME ZONE,
  stop_ts TIMESTAMP WITH TIME ZONE,
  CONSTRAINT _cfiles_pk PRIMARY KEY (id),
  CONSTRAINT _cfiles_cplid
    FOREIGN KEY (cplid) REFERENCES callrec.couples (id) ON DELETE CASCADE,
  CONSTRAINT _cfiles_cftype_ck CHECK (
    cftype IN (
      'MAG',
      'AUDIO',
      'ARCHIVE',
      'VIDEO',
      'IMAGE',
      'RECD'
    )
  )
) WITHOUT OIDS;


GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.cfiles TO GROUP callrecgrp;

COMMENT ON TABLE callrec.cfiles IS 'All files that are stored on filesystem have path link stored in cfiles.
 Information about file type and size.';
COMMENT ON COLUMN callrec.cfiles.cftype IS 'Domain cf_type (AUDIO, VIDEO...) - cfile type';
COMMENT ON COLUMN callrec.cfiles.cktype IS 'Domain cksum_t_typ_d - checksum type';
-- }}}
