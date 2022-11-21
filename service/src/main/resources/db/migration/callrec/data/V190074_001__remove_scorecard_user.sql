DELETE FROM callrec.user_roles WHERE userid = (SELECT id FROM callrec.users WHERE login = 'scorecard');
DELETE FROM callrec.passwords WHERE  userid = (select id FROM  callrec.users WHERE login = 'scorecard');
DELETE FROM callrec.users WHERE login = 'scorecard';
