-- Danish, Dutch, Norwegian, Swedish and Finnish are not longer supported in 6.x versions
DELETE FROM wbsc.languages WHERE (country = ANY ('{DK,NL,NO,SE,FI}'::text[]));