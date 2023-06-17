ALTER DOMAIN wbsc.answers_compliance_domain DROP CONSTRAINT answers_compliance_switch;
ALTER DOMAIN wbsc.answers_compliance_domain ADD CONSTRAINT answers_compliance_switch CHECK (((VALUE)::"text" = ANY (ARRAY[('SUCCESS_ALL'::character varying)::"text", ('SUCCESS_GROUP'::character varying)::"text", ('NONE'::character varying)::"text", ('NOT_APPLICABLE'::character varying)::"text", ('FAIL_GROUP'::character varying)::"text", ('FAIL_ALL'::character varying)::"text", ('REDUCE_ALL'::character varying)::"text"])));