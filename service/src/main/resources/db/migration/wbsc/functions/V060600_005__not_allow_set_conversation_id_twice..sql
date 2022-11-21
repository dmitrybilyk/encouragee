CREATE OR REPLACE FUNCTION wbsc.not_to_update_conversation_id() RETURNS TRIGGER
  AS $$
BEGIN
  IF OLD.conversation_id IS NOT NULL
THEN Raise Exception 'CONVERSATION_IS_ALREADY_SET';
END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION wbsc.not_to_update_conversation_id() TO GROUP wbscgrp;
ALTER FUNCTION wbsc.not_to_update_conversation_id() OWNER TO postgres;
GRANT EXECUTE ON FUNCTION wbsc.not_to_update_conversation_id() TO postgres;
GRANT EXECUTE ON FUNCTION wbsc.not_to_update_conversation_id() TO public;

CREATE TRIGGER calibrated_conv_id_set BEFORE UPDATE ON wbsc.calibrated_interactions
  FOR EACH ROW EXECUTE PROCEDURE wbsc.not_to_update_conversation_id();
