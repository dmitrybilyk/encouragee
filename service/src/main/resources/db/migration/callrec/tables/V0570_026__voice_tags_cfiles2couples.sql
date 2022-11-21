CREATE TABLE voice_tags_temp
(
  voice_tagid integer NOT NULL DEFAULT nextval('voice_tags_seq'::regclass),
  cplid integer NOT NULL,
  phraseid integer NOT NULL,
  from_time integer NOT NULL,
  to_time integer NOT NULL,
  confidence double precision,
  channel character varying(1)
);

INSERT INTO voice_tags_temp (voice_tagid, cplid, phraseid, from_time, to_time, confidence, channel)
select vt.voice_tagid, (select cf.cplid from cfiles cf where vt.cfileid = cf.id), vt.phraseid, vt.from_time, vt.to_time, vt.confidence, vt.channel from voice_tags vt;

DROP TABLE voice_tags;

alter table callrec.voice_tags_temp add constraint voice_tags_pk PRIMARY KEY (voice_tagid);
alter table callrec.voice_tags_temp add constraint voice_tags_couples_fk FOREIGN KEY (cplid) REFERENCES callrec.couples (id) ON UPDATE CASCADE ON DELETE CASCADE;
alter table callrec.voice_tags_temp add constraint voice_tags_phrases_fk FOREIGN KEY (phraseid)
    REFERENCES speech_phrases (phraseid) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE;
alter table callrec.voice_tags_temp add constraint voice_tags_channel_check CHECK (channel::text = 'L'::text OR channel::text = 'R'::text);
alter table callrec.voice_tags_temp add constraint voice_tags_check CHECK (to_time >= from_time);
alter table callrec.voice_tags_temp add constraint voice_tags_confidence_check CHECK (confidence >= 0::double precision OR confidence <= 100::double precision);
alter table callrec.voice_tags_temp add constraint voice_tags_from_time_check CHECK (from_time >= 0);

create index voice_phrase ON callrec.voice_tags_temp using btree (cplid, phraseid);
create index phraseIndex ON callrec.voice_tags_temp using btree (phraseid, confidence);

ALTER TABLE voice_tags_temp
  OWNER TO postgres;
GRANT ALL ON TABLE voice_tags_temp TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE voice_tags_temp TO callrecgrp;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE voice_tags_temp TO wbscgrp;
COMMENT ON TABLE voice_tags_temp
  IS 'Voice tags refers to one audio file and one phrase. It holds information about phrase accuracy in concrete time range and channel (left/right speaker)';

CREATE TRIGGER trig_on_voice_tags_insert
  AFTER INSERT
  ON voice_tags_temp
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.voice_tags_inserted();

CREATE TRIGGER trig_on_voice_tags_update_delete
  AFTER UPDATE OR DELETE
  ON voice_tags_temp
  FOR EACH ROW
  EXECUTE PROCEDURE callrec.voice_tags_updated_deleted();

ALTER TABLE voice_tags_temp RENAME TO voice_tags;
