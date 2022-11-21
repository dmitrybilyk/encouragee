ALTER TABLE wbsc.ccgroups DROP CONSTRAINT ccgroups_ccgroupname_key;

ALTER TABLE wbsc.ccgroups
  ADD CONSTRAINT ccgroups_ccgroupname_key
    UNIQUE (ccgroupname, company, keycloak_group_id);
