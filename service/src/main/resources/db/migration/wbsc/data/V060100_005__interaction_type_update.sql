DO
$do$
DECLARE
	companyid INTEGER;
BEGIN
	SELECT company
	FROM wbsc.interaction_types
	WHERE name = 'Call'
	INTO companyid;

	UPDATE wbsc.interaction_types SET name = 'Custom 1' WHERE name = 'Chat';
	INSERT INTO wbsc.interaction_types (name, type, company) SELECT 'Chat', 'SYSTEM', companyid;

  UPDATE wbsc.interaction_types SET name = 'Custom 2' WHERE name = 'Email';
	INSERT INTO wbsc.interaction_types (name, type, company) SELECT 'Email', 'SYSTEM', companyid;
End;
$do$;
