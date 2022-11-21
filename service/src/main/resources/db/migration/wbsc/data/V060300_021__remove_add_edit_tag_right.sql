DELETE FROM wbsc.rightvalue  WHERE rightid = (select rightid from wbsc.rights where name='INTERACTION_TAGS_ADD');
DELETE FROM wbsc.user_right  WHERE rightid = (select rightid from wbsc.rights where name='INTERACTION_TAGS_ADD');
DELETE FROM wbsc.role_right  WHERE rightid = (select rightid from wbsc.rights where name='INTERACTION_TAGS_ADD');
DELETE FROM wbsc.rights  WHERE name = 'INTERACTION_TAGS_ADD';

DELETE FROM wbsc.rightvalue  WHERE rightid = (select rightid from wbsc.rights where name='INTERACTION_TAGS_EDIT');
DELETE FROM wbsc.user_right  WHERE rightid = (select rightid from wbsc.rights where name='INTERACTION_TAGS_EDIT');
DELETE FROM wbsc.role_right  WHERE rightid = (select rightid from wbsc.rights where name='INTERACTION_TAGS_EDIT');
DELETE FROM wbsc.rights  WHERE name = 'INTERACTION_TAGS_EDIT';
