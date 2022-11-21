comment on table callrec.cfiles is 'All files that are stored on filesystem have path link stored in cfiles.
 It contains information about file type, size, path, encryption, start and stop of recordings.';

comment on table callrec.couple_extdata is 'Additional data to each couple.';

comment on table callrec.couple_fixed_extdata is 'Holds data used for searching based on external data, columns of data are defined in extdata_map table.';

comment on table callrec.decode_queue is 'PCAP files that are not decoded into media files.';

comment on table callrec.deleted_couples is 'Historical information of deleted couples with SID and time.';

comment on table callrec.extdata_map is 'Information about which column in couple_fixed_extdata contains which external data.
 Maximum number of column is defined in UI';

comment on table callrec.managed_keys is 'Security keys used in Call Recording Server.';

comment on table callrec.passwords is 'Historical data of encrypted user passwords with salts used in encrypting.';

comment on table callrec.role_actions is 'This table is used to match role with actions, to which role has privileges on.';

comment on table callrec.speech_phrases is 'It''s one or more words indexed by the speech analytics module';

comment on table callrec.tag_to_phrases is 'Match of each tag to each speech phrase.';

comment on table callrec.user_sessions is 'API saves its session.';

comment on table callrec.user_roles is 'Roles granted to agents.';

comment on table callrec.users is 'All users information.';

comment on table callrec.users_audit is 'Login as unique identification of user and his action.';