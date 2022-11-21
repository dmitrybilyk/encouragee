CREATE TABLE wbsc.search_templates
(
  templateid integer NOT NULL DEFAULT nextval('wbsc.seq_searchtemplates'::regclass),
  templatename character varying(100) NOT NULL DEFAULT ''::character varying,
  description character varying(255),
  ownerid integer,
  templatetype character varying(20),
  ccgroupid integer,
  userid integer,
  clientphone character varying(100),
  hoursfrom character varying(100),
  hoursto character varying(100),
  datefrom timestamp with time zone,
  dateto timestamp with time zone,
  CONSTRAINT "PKst" PRIMARY KEY (templateid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wbsc.search_templates
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.search_templates TO postgres;
GRANT ALL ON TABLE wbsc.search_templates TO wbscgrp;


CREATE TABLE wbsc.search_templ_to_inter_tags
(
  templateid integer NOT NULL,
  tagid integer NOT NULL,
  wanted boolean NOT NULL,
  CONSTRAINT "PKst_to_it" PRIMARY KEY ("templateid", tagid, wanted),
  CONSTRAINT "FKtags" FOREIGN KEY (tagid)
      REFERENCES wbsc.interaction_tags (tagid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT "FKtemplates" FOREIGN KEY ("templateid")
      REFERENCES wbsc.search_templates (templateid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wbsc.search_templ_to_inter_tags
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.search_templ_to_inter_tags TO postgres;
GRANT ALL ON TABLE wbsc.search_templ_to_inter_tags TO wbscgrp;


CREATE TABLE wbsc.search_templ_to_speech_tags
(
  templateid integer NOT NULL,
  tagid integer NOT NULL,
  certainty character varying(30) NOT NULL,
  wanted boolean NOT NULL,
  CONSTRAINT "PKst_to_st" PRIMARY KEY (templateid, tagid, wanted),
  CONSTRAINT "FKtags" FOREIGN KEY (tagid)
      REFERENCES callrec.speech_tags (tagid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT "FKtemplates" FOREIGN KEY (templateid)
      REFERENCES wbsc.search_templates (templateid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wbsc.search_templ_to_speech_tags
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.search_templ_to_speech_tags TO postgres;
GRANT ALL ON TABLE wbsc.search_templ_to_speech_tags TO wbscgrp;


CREATE TABLE wbsc.search_templ_to_ext_data
(
  templateid integer NOT NULL,
  identifier character varying(100) NOT NULL,
  value character varying(100) NOT NULL,
  iscombobox boolean NOT NULL,
  isadvancedpanel boolean NOT NULL,
  CONSTRAINT "PKst_to_ed" PRIMARY KEY (templateid, identifier, value, iscombobox),
  CONSTRAINT "FKtemplateid" FOREIGN KEY (templateid)
      REFERENCES wbsc.search_templates (templateid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wbsc.search_templ_to_ext_data
  OWNER TO postgres;
GRANT ALL ON TABLE wbsc.search_templ_to_ext_data TO postgres;
GRANT ALL ON TABLE wbsc.search_templ_to_ext_data TO wbscgrp;
