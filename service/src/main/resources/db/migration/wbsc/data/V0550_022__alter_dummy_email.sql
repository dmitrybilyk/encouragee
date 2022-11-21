UPDATE wbsc.sc_users SET email=replace(email, '@company.com', '@company.domain') WHERE email LIKE '%@company.com';
