alter table cfiles add column archived archive_mark default null;
alter table cfiles add column restored restore_mark default null;