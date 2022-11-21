-- CAL-14301
ALTER TABLE callrec.decoding_requests ADD COLUMN requests BIT(32);
ALTER TABLE callrec.decoding_requests ALTER COLUMN requests SET DEFAULT 0 :: BIT(32);
COMMENT ON COLUMN callrec.decoding_requests.requests IS 'Bit representation for request types (audio=1, calling phone video=2, called phone video=4)';
