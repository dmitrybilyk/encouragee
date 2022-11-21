--
-- TOC entry 423 (class 1247 OID 51226)
-- Dependencies: 424 6
-- Name: answers_compliance_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.answers_compliance_domain AS character varying(50)
	CONSTRAINT answers_compliance_switch CHECK (((VALUE)::text = ANY ((ARRAY['SUCCESS_ALL'::character varying, 'SUCCESS_GROUP'::character varying, 'NONE'::character varying, 'NOT_APPLICABLE'::character varying, 'FAIL_GROUP'::character varying, 'FAIL_ALL'::character varying])::text[])));



--
-- TOC entry 397 (class 1247 OID 26505)
-- Dependencies: 398 6
-- Name: criteria_call_directions_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.criteria_call_directions_domain AS character varying(50)
	CONSTRAINT criteria_call_directions_switch CHECK (((VALUE)::text = ANY ((ARRAY['INCOMING'::character varying, 'OUTGOING'::character varying, 'BOTH'::character varying, 'INTERNAL'::character varying, 'ALL'::character varying])::text[])));



--
-- TOC entry 431 (class 1247 OID 51295)
-- Dependencies: 432 6
-- Name: database_encryption_type_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.database_encryption_type_domain AS character varying(50)
	CONSTRAINT database_encryption_type_switch CHECK (((VALUE)::text = ANY ((ARRAY['PLAIN'::character varying, 'MD5'::character varying, 'SHA_2'::character varying, 'SHA_3'::character varying])::text[])));


--
-- TOC entry 378 (class 1247 OID 18048)
-- Dependencies: 379 6
-- Name: database_type_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.database_type_domain AS character varying(50)
	CONSTRAINT database_type_switch CHECK (((VALUE)::text = ANY ((ARRAY['LOCAL'::character varying, 'CALLREC'::character varying, 'IPCC'::character varying,'GENESYS'::character varying])::text[])));




--
-- TOC entry 400 (class 1247 OID 26520)
-- Dependencies: 401 6
-- Name: evaluations_eval_status_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.evaluations_eval_status_domain AS character varying(50)
	CONSTRAINT evaluations_eval_status_switch CHECK (((VALUE)::text = ANY ((ARRAY['CREATED'::character varying, 'IN_PROGRES'::character varying, 'FINISHED'::character varying, 'TEMPLATE'::character varying])::text[])));


--
-- TOC entry 400 (class 1247 OID 26520)
-- Dependencies: 401 6
-- Name: evaluations_eval_status_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.subevaluation_subeval_status_domain AS character varying(50)
	CONSTRAINT subevaluation_subeval_status_switch CHECK (((VALUE)::text = ANY ((ARRAY['CREATED'::character varying, 'IN_PROGRESS'::character varying, 'FINISHED'::character varying])::text[])));


--
-- TOC entry 370 (class 1247 OID 17945)
-- Dependencies: 371 6
-- Name: externaldata_comparator_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.externaldata_comparator_domain AS character varying(50)
	CONSTRAINT externaldata_comparator_switch CHECK (((VALUE)::text = ANY ((ARRAY['BIGGER'::character varying, 'BIGGER_OR_EQUAL'::character varying, 'EQUALS'::character varying, 'ISNULL'::character varying, 'NOTEQUAL'::character varying, 'NOTNULL'::character varying, 'SMALLER'::character varying, 'SMALLER_OR_EQUAL'::character varying, 'LIKE'::character varying, 'CONTAINS'::character varying, 'START_WITH'::character varying, 'END_WITH'::character varying,'ILIKE'::
character varying,'CONTAINS_IGNORE_CASE'::character varying, 'START_WITH_IGNORE_CASE'::character varying, 'END_WITH_IGNORE_CASE'::character varying	])::text[])));


--
-- TOC entry 372 (class 1247 OID 17947)
-- Dependencies: 373 6
-- Name: externaldata_format_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.externaldata_format_domain AS character varying(50)
	CONSTRAINT externaldata_format_switch CHECK (((VALUE)::text = ANY ((ARRAY['TEXT'::character varying, 'NUMBER'::character varying, 'DATE'::character varying, 'TIME'::character varying, 'DATETIME'::character varying])::text[])));


--
-- TOC entry 376 (class 1247 OID 17949)
-- Dependencies: 377 6
-- Name: externaldata_operator_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.externaldata_operator_domain AS character varying(50)
	CONSTRAINT externaldata_operator_switch CHECK (((VALUE)::text = ANY ((ARRAY['AND'::character varying, 'OR'::character varying])::text[])));



--
-- TOC entry 366 (class 1247 OID 17941)
-- Dependencies: 367 6
-- Name: questionform_access_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.questionform_access_domain AS character varying(50)
	CONSTRAINT questionform_access_switch CHECK (((VALUE)::text = ANY ((ARRAY['ALLOW_ALL'::character varying, 'DENY_ALL'::character varying, 'ALLOW_SELECTED'::character varying, 'DENY_SELECTED'::character varying])::text[])));


--
-- TOC entry 368 (class 1247 OID 17943)
-- Dependencies: 369 6
-- Name: questionform_matchingparts_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.questionform_matchingparts_domain
  AS character varying(50)
   CONSTRAINT questionform_matchingparts_switch CHECK (((VALUE)::text = ANY (ARRAY[('END_WITH'::character varying)::text, ('START_WITH'::character varying)::text, ('CONTAINS'::character varying)::text, ('CONTAINS_IGNORE_CASE'::character varying)::text,
    ('START_WITH_IGNORE_CASE'::character varying)::text, ('END_WITH_IGNORE_CASE'::character varying)::text, ('ILIKE'::character varying)::text])));

--
-- TOC entry 428 (class 1247 OID 51262)
-- Dependencies: 429 6
-- Name: questionform_scoring_system_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.questionform_scoring_system_domain AS character varying(50)
	CONSTRAINT questionform_scoring_system_switch CHECK (((VALUE)::text = ANY ((ARRAY['PERCENTAGE'::character varying, 'POINTS'::character varying, 'GRADES'::character varying])::text[])));

--
-- TOC entry 364 (class 1247 OID 17939)
-- Dependencies: 365 6
-- Name: questionform_sorting_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.questionform_sorting_domain AS character varying(50)
	CONSTRAINT questionform_sorting_switch CHECK (((VALUE)::text = ANY ((ARRAY['CREATION_TIME'::character varying, 'WEIGHT_ASC'::character varying, 'WEIGHT_DESC'::character varying])::text[])));


--
-- TOC entry 434 (class 1247 OID 51340)
-- Dependencies: 435 6
-- Name: user_identificator_domain; Type: DOMAIN; Schema: scorecard; Owner: -
--

CREATE DOMAIN wbsc.user_identificator_domain AS varchar(50)
	CONSTRAINT user_identificator_switch CHECK (((VALUE)::text = ANY ((ARRAY['EXTERNAL_AGENT_ID'::varchar,'PHONE'::varchar,'NONE'::varchar])::text[])));


CREATE DOMAIN wbsc.user_status_domain
  AS character varying(50)
   CONSTRAINT answers_compliance_switch CHECK (((VALUE)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying, 'DELETED'::character varying,'BLOCKED'::character varying])::text[])));

CREATE DOMAIN wbsc.template_type
  AS character varying(50)
   CONSTRAINT database_encryption_type_switch CHECK (((VALUE)::text = ANY ((ARRAY['PRIVATE'::character varying, 'SHARED'::character varying])::text[])));

CREATE DOMAIN wbsc.criteria_period_domain
  AS character varying(50)
   CONSTRAINT answers_compliance_switch CHECK (((VALUE)::text = ANY ((ARRAY['YESTERDAY'::character varying, 'LAST_WEEK'::character varying, 'THIS_WEEK'::character varying, 'LAST_MONTH'::character varying, 'THIS_MONTH'::character varying, 'NEXT_MONTH'::character varying, 'THIS_YEAR'::character varying,
 'LAST_YEAR'::character varying, 'FIRST_QUARTER'::character varying, 'SECOND_QUARTER'::character varying, 'THIRD_QUARTER'::character varying, 'FOURTH_QUARTER'::character varying, 'THIS_QUARTER'::character varying,
'CUSTOM_LAST_WEEK'::character varying, 'CUSTOM_THIS_WEEK'::character varying, 'CUSTOM_NEXT_WEEK'::character varying, 'CUSTOM_LAST_MONTH'::character varying, 'CUSTOM_THIS_MONTH'::character varying, 'CUSTOM_NEXT_MONTH'::character varying])::text[])));


-- create domain for interaction_types.type
CREATE DOMAIN wbsc.interaction_type_domain
  AS character varying(50)
   CONSTRAINT answers_compliance_switch CHECK (((VALUE)::text = ANY (ARRAY[('SYSTEM'::character varying)::text, ('USER'::character varying)::text])));


CREATE DOMAIN "wbsc"."messagebox_status_domain" AS   varchar(10) 
 CONSTRAINT "messagebox_status_domain_chk" CHECK ((VALUE)::text = ANY ((ARRAY['NEW'::character varying, 'READ'::character varying, 'DELETED'::character varying])::text[]));

