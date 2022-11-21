
-- SC-3696
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'INTERACTIONS_COUNT_MAX_LIMIT', '1000', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));

-- fix for SC-2701. Database inconsistency on the subevaluation table. Assign a default value to interactiontypeid when it's null
update wbsc.subevaluation set interactiontypeid = (select interactiontypeid from wbsc.interaction_types where name = 'Call') where interactiontypeid is NULL;

-- SC-3962
DELETE FROM wbsc.rights WHERE name = 'REPLACE_CALLS_INSTANCE' OR name ='COMPARE_GROUPS';



-- SC-2128
UPDATE wbsc.audit SET type = 'CREATE_EVALUATION' WHERE type = 'ADD_EVALUATION';


-- New permissions for interactions

INSERT INTO wbsc.rights (name) VALUES ('INTERACTIONS_FULL_VIEW');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTIONS_AGENT_VIEW');
INSERT INTO wbsc.rights (name) VALUES ('INTERACTIONS_GROUP_VIEW');

INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='INTERACTIONS_AGENT_VIEW'), (select roleid from  wbsc.roles where name='Agent'));

INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='INTERACTIONS_GROUP_VIEW'), (select roleid from  wbsc.roles where name='Team leader'));

INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='INTERACTIONS_GROUP_VIEW'), (select roleid from  wbsc.roles where name='Supervisor'));

INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='INTERACTIONS_FULL_VIEW'), (select roleid from  wbsc.roles where name='CC Manager'));
INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='INTERACTIONS_GROUP_VIEW'), (select roleid from  wbsc.roles where name='CC Manager'));


-- New permissions for SpeechTags

INSERT INTO wbsc.rights (name) VALUES ('MANAGE_SPEECH_TAG');

INSERT INTO wbsc.role_right (rightid, roleid) VALUES ((select rightid from  wbsc.rights where name='MANAGE_SPEECH_TAG'), (select roleid from  wbsc.roles where name='CC Manager'));

--new options for REST API
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'REST_API_AUDIO_PATH', 'api/couple/{0}/mp3', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'REST_API_VIDEO_PATH', 'api/couple/{0}/recd', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));
INSERT INTO wbsc.appoptions (key, value, comment, company) values ( 'REST_API_XML_PATH', 'api/interaction/xml?couplesid={0}', NULL, (select companyid from wbsc.companies where display_name='DEFAULT'));

-- SC-4334

INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Dansk', 'da', 'DK', false,'Danish');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Suomi', 'fi', 'FI', false,'Finnish');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Latviešu', 'lv', 'LV', false,'Latvian');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Nederlands', 'nl', 'NL', false,'Dutch');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Português (Brasil)', 'pt', 'BR', false,'Portuguese (Brazil)');
INSERT INTO wbsc.languages ( display_name, language, country, main_language, description) VALUES (  'Svenska', 'sv', 'SE', false,'Swedish');


