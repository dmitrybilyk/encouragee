ALTER TABLE wbsc.tags_to_interactions ADD COLUMN couple_sid VARCHAR(255);

UPDATE wbsc.tags_to_interactions SET couple_sid =
(SELECT couples.sid FROM callrec.couples couples WHERE id = coupleid);

DELETE from wbsc.tags_to_interactions where couple_sid is null;

ALTER TABLE wbsc.tags_to_interactions DROP CONSTRAINT tags_to_interactions_tags_pk;

ALTER TABLE wbsc.tags_to_interactions ADD CONSTRAINT tags_to_interactions_tags_pk
PRIMARY KEY(tagid, couple_sid, datetime);

ALTER TABLE wbsc.tags_to_interactions DROP COLUMN coupleid;
