DELETE FROM wbsc.rightvalue  WHERE rightid = (select rightid from  wbsc.rights where name='REPORTING_OWN_TEAM');
DELETE FROM wbsc.user_right  WHERE rightid = (select rightid from  wbsc.rights where name='REPORTING_OWN_TEAM');
DELETE FROM wbsc.role_right  WHERE rightid = (select rightid from  wbsc.rights where name='REPORTING_OWN_TEAM');
DELETE FROM wbsc.rights  WHERE name = 'REPORTING_OWN_TEAM';
