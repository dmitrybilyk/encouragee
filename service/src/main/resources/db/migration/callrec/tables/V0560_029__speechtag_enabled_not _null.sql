UPDATE callrec.speech_phrases SET enabled = true WHERE enabled IS NULL;
ALTER TABLE callrec.speech_phrases ALTER COLUMN enabled SET DEFAULT true;
ALTER TABLE callrec.speech_phrases ALTER COLUMN enabled SET NOT NULL;
