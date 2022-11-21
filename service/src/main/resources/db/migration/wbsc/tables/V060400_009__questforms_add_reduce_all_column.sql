ALTER TABLE wbsc.questforms ADD COLUMN reduce_all NUMERIC(5,2);
ALTER TABLE wbsc.questforms ADD COLUMN to_reduce_to boolean DEFAULT true;
