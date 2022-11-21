CREATE VIEW wbsc.audit_migration AS
  SELECT a.*, u.name, u.surname, u.login
  FROM wbsc.audit a
  	LEFT JOIN wbsc.sc_users u on a.logged_user = u.userid;

GRANT SELECT ON TABLE wbsc.audit_migration TO wbscgrp;
