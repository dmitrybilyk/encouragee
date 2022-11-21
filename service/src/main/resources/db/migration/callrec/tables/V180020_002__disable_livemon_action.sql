alter table callrec.actions add enabled boolean default true not null;
UPDATE callrec.actions SET enabled = false where name = 'call_monitor';
