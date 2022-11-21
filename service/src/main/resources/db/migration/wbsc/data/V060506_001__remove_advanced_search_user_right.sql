DELETE FROM wbsc.rightvalue  WHERE rightid = (select rightid from  wbsc.rights where name='ADVANCED_SEARCH_USER');
DELETE FROM wbsc.user_right  WHERE rightid = (select rightid from  wbsc.rights where name='ADVANCED_SEARCH_USER');
DELETE FROM wbsc.role_right  WHERE rightid = (select rightid from  wbsc.rights where name='ADVANCED_SEARCH_USER');
DELETE FROM wbsc.rights  WHERE name = 'ADVANCED_SEARCH_USER';
