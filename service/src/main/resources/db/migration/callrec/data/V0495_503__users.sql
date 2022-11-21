INSERT INTO callrec.users (
  id,
  login,
  password,
  firstname,
  lastname,
  email,
  phonenr
) VALUES (
  callrec.GET_NEXT_USERID(),
  'admin',
  MD5('admin'),
  '',
  '',
  '',
  NULL
);

INSERT INTO callrec.users (
  id,
  login,
  password,
  firstname,
  lastname,
  email,
  phonenr
) VALUES (
  callrec.GET_NEXT_USERID(),
  'scorecard',
  null,
  '',
  '',
  '',
  NULL
);

INSERT INTO callrec.passwords
  (userid, 
   password, 
   salt,
   creation_date
   )
values
  ((select id from  callrec.users where login='scorecard'),
	'41f444c633f1d8eed7a29cd9a35ea21d5f667b7cdea5ae8cca8b08d3a333af2b',
	'a71c5a592323bf28',
	now()); 
