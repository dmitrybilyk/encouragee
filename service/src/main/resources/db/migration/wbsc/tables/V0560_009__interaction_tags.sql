CREATE TABLE wbsc.interaction_tags
(
  tagId integer NOT NULL DEFAULT nextval('seq_interaction_tags'::regclass),
  tagText character varying(255) NOT NULL,
  privateTag boolean NOT NULL DEFAULT TRUE,
  creatorId integer NOT NULL,
  creatorName character varying(150) NOT NULL,
  datetime timestamp with time zone NOT NULL,
  companyId integer NOT NULL,
  CONSTRAINT interaction_tags_pkey PRIMARY KEY (tagId),
  CONSTRAINT interaction_tags_companyId_fk FOREIGN KEY (companyId)
    REFERENCES wbsc.companies (companyId) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT interaction_tags_creatorId_fk FOREIGN KEY (creatorId)
    REFERENCES wbsc.sc_users (userid) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE SET NULL
)
WITHOUT OIDS;

ALTER TABLE wbsc.interaction_tags
  OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.interaction_tags TO wbscgrp;

CREATE TABLE wbsc.tags_to_interactions
(
  tagId      integer NOT NULL,
  coupleId   integer NOT NULL,
  datetime   timestamp with time zone NOT NULL,
  CONSTRAINT tags_to_interactions_pkey PRIMARY KEY (tagId, coupleId),
  CONSTRAINT tags_to_interactions_tags_fk FOREIGN KEY (tagId)
    REFERENCES wbsc.interaction_tags (tagId) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE wbsc.tags_to_interactions
  OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE wbsc.tags_to_interactions TO wbscgrp;
