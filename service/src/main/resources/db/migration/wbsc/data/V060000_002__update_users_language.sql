-- Danish, Dutch, Norwegian, Swedish and Finnish are not longer supported in 6.x versions
-- users with these languages will have en_US language
UPDATE wbsc.sc_users SET
language = (SELECT languageid FROM wbsc.languages WHERE country = 'US')
WHERE
language IN (SELECT languageid FROM wbsc.languages WHERE (country = ANY ('{DK,NL,NO,SE,FI}'::text[])));
