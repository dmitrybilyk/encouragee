CREATE TABLE wbsc.templates
(
  templateid integer NOT NULL DEFAULT nextval('wbsc.seq_templates'::regclass),
  templatename character varying(255) NOT NULL,
  template_type wbsc.template_type,
  "owner" integer NOT NULL,
  evaluationid integer,
  description text,
  CONSTRAINT sc_templates_pk PRIMARY KEY (templateid),
  CONSTRAINT sc_templates_evaluation_fk FOREIGN KEY (evaluationid)
      REFERENCES wbsc.evaluations (evaluationid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT templates_owner_fkey FOREIGN KEY ("owner")
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT sc_template_name UNIQUE (templatename)
)
WITHOUT OIDS;
ALTER TABLE wbsc.templates OWNER TO postgres;


CREATE TABLE wbsc.template_for_group
(
  templateid integer NOT NULL,
  evaluator integer NOT NULL,
  ccgroupid integer NOT NULL,
  CONSTRAINT sc_template_for_group_pk PRIMARY KEY (templateid, evaluator, ccgroupid),
  CONSTRAINT sc_template_for_groups_ccgroup FOREIGN KEY (ccgroupid)
      REFERENCES wbsc.ccgroups (ccgroupid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sc_template_for_groups_evaluator FOREIGN KEY (evaluator)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sc_template_for_groups_templates FOREIGN KEY (templateid)
      REFERENCES wbsc.templates (templateid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE wbsc.template_for_group OWNER TO postgres;



CREATE TABLE wbsc.template_for_user
(
  templateid integer NOT NULL,
  evaluator integer NOT NULL,
  userid integer NOT NULL,
  CONSTRAINT sc_template_for_user_pk PRIMARY KEY (templateid, evaluator, userid),
  CONSTRAINT sc_template_for_user_evaluator FOREIGN KEY (evaluator)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sc_template_for_user_template FOREIGN KEY (templateid)
      REFERENCES wbsc.templates (templateid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sc_template_for_user_userid FOREIGN KEY (userid)
      REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITHOUT OIDS;
ALTER TABLE wbsc.template_for_user OWNER TO postgres;

GRANT select, update, insert, delete on wbsc.templates to wbscgrp;
GRANT select, update, insert, delete on wbsc.template_for_group to wbscgrp;
GRANT select, update, insert, delete on wbsc.template_for_user to wbscgrp;

