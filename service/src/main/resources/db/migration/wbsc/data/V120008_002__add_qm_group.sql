--  Adding QM group to Root Group
WITH
    ROOT AS (  SELECT   ccgroupid, company, external_id
               FROM wbsc.ccgroups
               WHERE parentId IS NULL)
INSERT INTO wbsc.ccgroups (ccgroupname, description, parentid, company, external_id) VALUES ('QM_Root_Group_Original','QM group', (select ccgroupid from ROOT),  (select company from ROOT),  (select external_id from ROOT));

-- Recalculate left and right indexes
WITH QM_GROUP_ID AS (SELECT
                         ccgroupid
                     FROM wbsc.ccgroups
                     WHERE ccgroupname = 'QM_Root_Group_Original')
SELECT wbsc."migrate_root_group_children"((SELECT ccgroupid from QM_GROUP_ID));


WITH QM_GROUP_ID AS (SELECT
                         ccgroupid
                     FROM wbsc.ccgroups
                     WHERE ccgroupname = 'QM_Root_Group_Original'),
     ROOT_ID AS (  SELECT   ccgroupid
                   FROM wbsc.ccgroups
                   WHERE parentId IS NULL)
UPDATE wbsc.user_belongsto_ccgroup SET  ccgroupid = (select ccgroupid from QM_GROUP_ID) WHERE ccgroupid = (SELECT ccgroupid from ROOT_ID);

drop function IF EXISTS wbsc.migrate_root_group_children(qm_group_id INT);

WITH QM_GROUP_ID AS (SELECT
                         ccgroupid
                     FROM wbsc.ccgroups
                     WHERE ccgroupname = 'QM_Root_Group_Original'),
     ROOT_ID AS (  SELECT   ccgroupid
                   FROM wbsc.ccgroups
                   WHERE parentId IS NULL)
UPDATE wbsc.user_canevaluate_ccgroup SET  ccgroupid = (select ccgroupid from QM_GROUP_ID) WHERE ccgroupid = (SELECT ccgroupid from ROOT_ID);
