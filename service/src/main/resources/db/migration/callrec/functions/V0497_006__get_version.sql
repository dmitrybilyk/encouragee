CREATE OR REPLACE FUNCTION callrec.get_version()
  RETURNS VARCHAR
  LANGUAGE plpgsql
  AS '
    BEGIN
      RETURN ''4.9.7'';
    END;
  ';
