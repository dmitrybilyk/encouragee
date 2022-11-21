INSERT INTO wbsc.questforms (qformname, version, accessibility, scoring_system, detailed_report, sorting, internal_calls_scoring, weekdays, time_from, time_to, min_call_length, max_call_length, description, company, completed, report_weight, qftype)
VALUES
  ('NPS Survey Template', '1.0', 'ALLOW_ALL', 'POINTS', '1', 'CREATION_TIME', '0', '1111111', NULL, NULL, NULL, NULL, NULL,
   (SELECT companyid FROM wbsc.companies WHERE display_name='DEFAULT'), '0', 1, 'SURVEY');

INSERT INTO wbsc.questiongroups (groupname, qformid, group_order, description, group_value)
VALUES ('NPS question', (SELECT qformid
                         FROM wbsc.questforms
                         WHERE qformname = 'NPS Survey Template'), 1, NULL, 100);

INSERT INTO wbsc.questions (question_text, groupid, question_order, description, smediafileid, question_type, question_value)
VALUES ('On a scale of zero to 10, with 0 representing "Not at all Likely" and Ten representing "Extremely likely", How likely are you to recommend our company to a friend or colleague?',
        (SELECT questiongroupid
         FROM wbsc.questiongroups
         WHERE groupname = 'NPS question'), 1, 'NPS score question', NULL, 'NPS_QUESTION', 100);

INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('0', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 0, NULL, 'NONE', '0', 0, 0);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('1', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 1, NULL, 'NONE', '1', 1, 1);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('2', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 2, NULL, 'NONE', '2', 2, 2);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('3', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 3, NULL, 'NONE', '3', 3, 3);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('4', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 4, NULL, 'NONE', '4', 4, 4);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('5', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 5, NULL, 'NONE', '5', 5, 5);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('6', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 6, NULL, 'NONE', '6', 6, 6);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('7', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 7, NULL, 'NONE', '7', 7, 7);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('8', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 8, NULL, 'NONE', '8', 8, 8);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('9', (SELECT questionid
              FROM wbsc.questions
              WHERE description = 'NPS score question'), 9, NULL, 'NONE', '9', 9, 9);
INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('10', (SELECT questionid
               FROM wbsc.questions
               WHERE description = 'NPS score question'), 10, NULL, 'NONE', '10', 10, 10);

INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('n/a', (SELECT questionid
                FROM wbsc.questions
                WHERE description = 'NPS score question'), 11, NULL, 'NOT_APPLICABLE', 'NONE', 0, 11);


INSERT INTO wbsc.questions (question_text, groupid, question_order, description, smediafileid, question_type, question_value)
VALUES ('Please provide any additional comments', (SELECT questiongroupid
                                                   FROM wbsc.questiongroups
                                                   WHERE groupname = 'NPS question'), 2, 'NPS comment', NULL, 'CUSTOMER_FEEDBACK', 0);


INSERT INTO wbsc.answers (answer_text, questionid, answer_order, description, compliance, answer_key, answer_value, create_order)
VALUES ('Customer hangs up', (SELECT questionid
                              FROM wbsc.questions
                              WHERE description = 'NPS comment'), 0, NULL, 'NOT_APPLICABLE', 'NONE', 0, 0);

INSERT INTO wbsc.questions (question_text, groupid, question_order, description, smediafileid, question_type, question_value)
VALUES ('Thank you for taking part in our survey', (SELECT questiongroupid
                                                    FROM wbsc.questiongroups
                                                    WHERE groupname = 'NPS question'), 3, 'thank you prompt', NULL, 'PROMPT_ONLY', 0);
