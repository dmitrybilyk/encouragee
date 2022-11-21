DO
$do$
declare

to_process INTEGER;

begin

select count(1) into to_process from callrec.couples where archived = 'A' and cfcnt >0;

if to_process > 500000 THEN
	RAISE NOTICE 'AAA----------------------------------';
	RAISE NOTICE 'THERE IS % COUPLES THAT ARE ARCHIVED WITH CFILE IN DB !', to_process;
	RAISE NOTICE 'PLEASE RUN LONG RUNNING SCRIPT TO CORRECTLY HANDLE ARCHIVED COUPLES';
	RAISE NOTICE 'Read instructions in and manually run file';
	RAISE NOTICE '/opt/callrec/dbscripts/postgres/update/5.8/V0580_archive_flag_to_cfiles.sql';
	RAISE NOTICE 'BBB----------------------------------';
ELSE
	update callrec.cfiles  set archived = c.archived from callrec.couples c  where c.archived is not null and c.id = cplid;
END IF;

update callrec.cfiles  set restored = c.restored from callrec.couples c  where c.restored is not null and c.id = cplid;
End;
$do$;