CREATE TABLE wbsc.auditreason_temp (
  value character varying(255),
  visible boolean,
  companyid integer
);

-- insert values of duplicated rows to temp table
INSERT INTO wbsc.auditreason_temp
    SELECT value, bool_or(visible) AS visible, companyid
    FROM wbsc.auditreason
    GROUP BY value, companyid
    HAVING count(*) > 1;

-- delete duplicated rows
DELETE FROM wbsc.auditreason WHERE value IN (
  SELECT value
  FROM wbsc.auditreason
  GROUP BY value, companyid
  HAVING count(*) > 1
);

-- insert values back
INSERT INTO wbsc.auditreason(value, visible, companyid)
  SELECT * FROM wbsc.auditreason_temp;

DROP TABLE wbsc.auditreason_temp;
