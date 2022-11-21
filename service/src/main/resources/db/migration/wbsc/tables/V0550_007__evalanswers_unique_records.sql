DELETE FROM wbsc.evalanswers
WHERE evalanswerid IN (SELECT evalanswerid
              FROM (SELECT evalanswerid,
                             ROW_NUMBER() OVER (partition BY subevaluationid, answerid ORDER BY evalanswerid) AS rnum
                     FROM wbsc.evalanswers) t
              WHERE t.rnum > 1);

ALTER TABLE wbsc.evalanswers
  ADD CONSTRAINT evalanswers_unique_answers
    UNIQUE ("subevaluationid", "answerid");
