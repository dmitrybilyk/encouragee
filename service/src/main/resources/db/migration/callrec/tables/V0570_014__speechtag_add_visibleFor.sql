ALTER TABLE callrec.speech_tags ADD COLUMN visibleFor VARCHAR(255) default 'ALL_GROUPS';
ALTER TABLE callrec.speech_tags ADD COLUMN serializedVisibleForGroups TEXT;