--foreign keys

CREATE INDEX sc_answers_questionid
  ON wbsc.answers
  USING btree
  (questionid);

CREATE INDEX sc_appoptions_company
  ON wbsc.appoptions
  USING btree
  (company);

CREATE INDEX sc_audit_logged_user
  ON wbsc.audit
  USING btree
  (logged_user);


CREATE INDEX sc_callwrapups_company
  ON wbsc.callwrapups
  USING btree
  (company);

CREATE INDEX sc_categories_company
  ON wbsc.categories
  USING btree
  (company);

CREATE INDEX sc_ccgroups_company
  ON wbsc.ccgroups
  USING btree
  (company);

CREATE INDEX sc_ccgroups_parentid
  ON wbsc.ccgroups
  USING btree
  (parentid);

CREATE INDEX sc_criteria_evaluationid
  ON wbsc.criteria
  USING btree
  (evaluationid);


CREATE INDEX sc_criteria_call_wrapup
  ON wbsc.criteria
  USING btree
  (call_wrapup);

CREATE INDEX sc_database_company
  ON wbsc."database"
  USING btree
  (company);

CREATE INDEX sc_evalanswers_answerid
  ON wbsc.evalanswers
  USING btree
  (answerid);

CREATE INDEX sc_evalanswers_subevaluationid
  ON wbsc.evalanswers
  USING btree
  (subevaluationid);

CREATE INDEX sc_evalcalls_subevaluationid
  ON wbsc.evalcalls
  USING btree
  (subevaluationid);

CREATE INDEX sc_evaluation_copyof
  ON wbsc.evaluations
  USING btree
  (copyof);

CREATE INDEX sc_evaluation_evaluated_user
  ON wbsc.evaluations
  USING btree
  (evaluated_user);

CREATE INDEX sc_evaluation_evaluatorid
  ON wbsc.evaluations
  USING btree
  (evaluatorid);

CREATE INDEX sc_evaluation_qformid
  ON wbsc.evaluations
  USING btree
  (qformid);

CREATE INDEX sc_evaluation_created_by
  ON wbsc.evaluations
  USING btree
  (created_by);

CREATE INDEX sc_evaluations_company
  ON wbsc.evaluations
  USING btree
  (company);


CREATE INDEX sc_externaldata_criteriaid
  ON wbsc.externaldata
  USING btree
  (criteriaid);


CREATE INDEX sc_medialimits_criteriaid
  ON wbsc.medialimits
  USING btree
  (criteriaid);

CREATE INDEX sc_messagebox_from_user
  ON wbsc.messagebox
  USING btree
  (from_user);

CREATE INDEX sc_messagebox_to_user
  ON wbsc.messagebox
  USING btree
  (to_user);

CREATE INDEX sc_questforms_company
  ON wbsc.questforms
  USING btree
  (company);

CREATE INDEX sc_questiongroups
  ON wbsc.questiongroups
  USING btree
  (qformid);

CREATE INDEX sc_questions_groupid
  ON wbsc.questions
  USING btree
  (groupid);


CREATE INDEX sc_rightvalue_rightid
  ON wbsc.rightvalue
  USING btree
  (rightvalueid);

CREATE INDEX sc_role_company
  ON wbsc.roles
  USING btree
  (company);


CREATE INDEX sc_users_company
  ON wbsc.sc_users
  USING btree
  (company);

CREATE INDEX sc_users_databaseid
  ON wbsc.sc_users
  USING btree
  (database);

CREATE INDEX sc_users_delegetor
  ON wbsc.sc_users
  USING btree
  (delegator);

CREATE INDEX sc_users_language
  ON wbsc.sc_users
  USING btree
  (language);


CREATE INDEX sc_users_send_feedback
  ON wbsc.sc_users
  USING btree
  (send_feedback);

CREATE INDEX sc_subevaluation_categoryid
  ON wbsc.subevaluation
  USING btree
  (categoryid);

CREATE INDEX sc_subevaluation_criteriaid
  ON wbsc.subevaluation
  USING btree
  (criteriaid);

CREATE INDEX sc_user_settings
  ON wbsc.user_settings
  USING btree
  (userid);


-- searching

--in audit is combination of date, user and type occasion...


--ordering


CREATE INDEX sc_users_name
  ON wbsc.sc_users
  USING btree
  (name varchar_pattern_ops);


CREATE INDEX sc_users_surname
  ON wbsc.sc_users
  USING btree
  (surname varchar_pattern_ops);

CREATE INDEX sc_users_login
  ON wbsc.sc_users
  USING btree
  ("login" varchar_pattern_ops);

CREATE INDEX sc_users_status
  ON wbsc.sc_users
  USING btree
  (status);


CREATE INDEX sc_audit_datetime
  ON wbsc.audit
  USING btree
  (datetime);

CREATE INDEX sc_audit_type
  ON wbsc.audit
  USING btree
  (type);

CREATE INDEX sc_audit_result
  ON wbsc.audit
  USING btree
  (result);


CREATE INDEX sc_evaluations_evalstatus
  ON wbsc.evaluations
  USING btree
  (evalstatus);

CREATE INDEX sc_rights_name
  ON wbsc.rights
  USING btree
  (name);


CREATE INDEX sc_criteria_ismain
  ON wbsc.criteria
  USING btree
  (is_main);




















