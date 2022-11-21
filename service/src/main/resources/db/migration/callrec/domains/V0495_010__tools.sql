CREATE DOMAIN callrec.synchro_mark AS VARCHAR(1)
  CONSTRAINT dom_synchro_mark CHECK (
    VALUE IN (
      'S','N', 'E','F'
    )
  );

CREATE DOMAIN callrec.mixer_mark AS VARCHAR
  CONSTRAINT dom_mixer_mark CHECK (
    VALUE IN (
      'M','F'
    )
  );

CREATE DOMAIN callrec.delete_mark AS VARCHAR
  CONSTRAINT dom_delete_mark CHECK (
    VALUE IN (
      'D','F'
    )
  );

CREATE DOMAIN callrec.restore_mark AS VARCHAR
  CONSTRAINT dom_restore_mark CHECK (
    VALUE IN (
      'R','F','Q'
    )
  );

CREATE DOMAIN callrec.archive_mark AS VARCHAR
  CONSTRAINT dom_archive_mark CHECK (
    VALUE IN (
     'A','F'
    )
  );

CREATE DOMAIN callrec.evaluate_mark AS VARCHAR
  CONSTRAINT dom_evaluate_mark CHECK (
    VALUE IN (
      'E','B'
    )
  );