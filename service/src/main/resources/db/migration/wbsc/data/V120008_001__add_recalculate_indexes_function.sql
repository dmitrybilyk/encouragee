-- Add function for migrating groups from root group to QM
CREATE OR REPLACE FUNCTION wbsc.migrate_root_group_children(qm_group_id INT) RETURNS INT AS $$
DECLARE
    root_group_id INT;
    currentId INT;
BEGIN
    SELECT ccgroupId INTO root_group_id FROM wbsc.ccgroups WHERE parentid IS NULL;
    FOR currentId IN SELECT ccgroupid FROM wbsc.ccgroups WHERE parentid = root_group_id AND ccgroupid != qm_group_id ORDER BY ccgroupid ASC
        LOOP
            PERFORM "ccgroups_update"(currentId, qm_group_id);
        END LOOP;
    RETURN 1;
END
$$ LANGUAGE plpgsql
