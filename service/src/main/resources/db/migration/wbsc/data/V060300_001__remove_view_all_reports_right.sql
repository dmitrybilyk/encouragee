DELETE FROM wbsc.rightvalue  WHERE rightid = (select rightid from  wbsc.rights where name='VIEW_ALL_REPORTS');
DELETE FROM wbsc.user_right  WHERE rightid = (select rightid from  wbsc.rights where name='VIEW_ALL_REPORTS');
DELETE FROM wbsc.role_right  WHERE rightid = (select rightid from  wbsc.rights where name='VIEW_ALL_REPORTS');
DELETE FROM wbsc.rights  WHERE name = 'VIEW_ALL_REPORTS';
