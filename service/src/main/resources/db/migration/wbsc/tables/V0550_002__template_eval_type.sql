ALTER TABLE wbsc.templates ADD evaluation_type evaluation_type DEFAULT 'QUALITY' NOT NULL;

UPDATE wbsc.templates SET evaluation_type = 'QUALITY';
