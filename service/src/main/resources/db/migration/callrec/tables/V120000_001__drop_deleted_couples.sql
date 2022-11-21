DROP TRIGGER IF EXISTS trig_on_couple_delete on callrec.couples;

DROP FUNCTION IF EXISTS callrec.couple_deleted();

DROP TABLE IF EXISTS callrec.deleted_couples;
