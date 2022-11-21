-- $Id: get_version.sql,v 1.6 2010-09-22 11:56:25 filip Exp $

CREATE OR REPLACE FUNCTION callrec.get_version()
  RETURNS VARCHAR
  LANGUAGE plpgsql
  AS '
    BEGIN
      RETURN ''4.9.6'';
    END;
  ';
