CREATE DOMAIN callrec.rec_rules_policy AS VARCHAR
  CONSTRAINT rec_rules_policy_ck CHECK (
    VALUE IN (
      'RECORD',
      'PRE_RECORD',
      'NO_RECORD',
      'IGNORE'
    )
  );

