ALTER TABLE wbsc.tags_to_interactions DROP CONSTRAINT tags_to_interactions_pkey;

ALTER TABLE wbsc.tags_to_interactions ADD CONSTRAINT tags_to_interactions_tags_pk PRIMARY KEY(tagid, coupleid, datetime);