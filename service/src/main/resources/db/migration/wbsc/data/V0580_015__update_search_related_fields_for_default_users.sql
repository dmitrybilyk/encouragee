UPDATE wbsc.sc_users SET identificator_used = 'NONE', phone = NULL, agentid = NULL
WHERE login IN ('ccmanager', 'admin');