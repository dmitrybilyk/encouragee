-- CAL-14169
ALTER TABLE callrec.cfiles DROP CONSTRAINT _cfiles_cftype_ck;
ALTER TABLE callrec.cfiles ADD CONSTRAINT _cfiles_cftype_ck CHECK (
  cftype IN (
    'MAG',
    'AUDIO',
    'ARCHIVE',
    'VIDEO',
    'IMAGE',
    'RECD',
    'PVMAG',
    'PVSTREAM',
    'PVIDEO'
  )
);


