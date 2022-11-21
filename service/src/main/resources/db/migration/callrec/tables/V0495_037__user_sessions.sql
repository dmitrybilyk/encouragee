CREATE TABLE callrec.user_sessions (
    id VARCHAR(255) NOT NULL,
    key VARCHAR(255) NOT NULL,
    value TEXT,
    updated_ts TIMESTAMP WITH TIME ZONE DEFAULT now(),
    expires_ts TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id, key)
) WITHOUT OIDS;


GRANT SELECT, INSERT, UPDATE, DELETE ON callrec.user_sessions TO GROUP callrecgrp;


COMMENT ON TABLE callrec.user_sessions IS 'API saves its session in this table.';

