-- CREATE DOMAIN problemstatus {{{
CREATE DOMAIN callrec.problemstatus AS VARCHAR
  CONSTRAINT dom_problemstatus CHECK (
    VALUE IN (
      'NO_PROBLEM',
      'ALL_STREAM_DELETED',
      'STILL_SAVING_STREAM',
      'ONLY_ONE_STREAM',
      'STREAM_IS_NULL',
      'NO_STREAMS',
      'UNKNOWN_CODEC',
      'NO_REPLY_FROM_RECORDER',
      'DECODER_IO_ERROR',
      'DECODER_DIFF_PAYLOAD',
      'DECODER_OVERSIZE_TARGET',
      'DECODER_NO_DEST_FORMAT',
      'NO_REPLY_FROM_DECODER',
      'UNKNOWN_RULES',
      'CAPTURE_FILE_NOT_FOUND',
      'RECORDER_LICENSE_PROBLEM',
      'CAPTURE_FILE_INCOMPLETE',
      'ONE_STREAM_INCOMPLETE'
    )
  );
-- }}}



CREATE DOMAIN callrec.couple_state
  AS character varying
   CONSTRAINT dom_couple_state CHECK (((VALUE)::text = ANY ((ARRAY['EXT_DATA_SAVED'::character varying, 'FINISHED'::character varying])::text[])));
ALTER DOMAIN callrec.couple_state OWNER TO postgres;
