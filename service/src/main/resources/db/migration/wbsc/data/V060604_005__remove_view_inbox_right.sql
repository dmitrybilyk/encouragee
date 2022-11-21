DELETE FROM wbsc.rightvalue  WHERE rightid = (select rightid from  wbsc.rights where name='VIEW_INBOX');
DELETE FROM wbsc.user_right  WHERE rightid = (select rightid from  wbsc.rights where name='VIEW_INBOX');
DELETE FROM wbsc.role_right  WHERE rightid = (select rightid from  wbsc.rights where name='VIEW_INBOX');
DELETE FROM wbsc.rights  WHERE name = 'VIEW_INBOX';
