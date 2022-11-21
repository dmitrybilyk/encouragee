-- store text instead of id to keep reason unchanged in log
ALTER TABLE wbsc.audit ADD COLUMN reason character varying(255);

UPDATE wbsc.audit AS a
	SET reason=r.value
	FROM wbsc.auditreason AS r
	WHERE a.reasonid=r.auditreasonid;

ALTER TABLE wbsc.audit DROP COLUMN reasonid;

