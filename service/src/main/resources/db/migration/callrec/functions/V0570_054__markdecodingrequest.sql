CREATE OR REPLACE FUNCTION mark_decoding_request(BIGINT, INT) RETURNS VOID
LANGUAGE plpgsql
AS '
DECLARE
  _id ALIAS FOR $1;
  _requests ALIAS FOR $2;
  _curr_value BIT(32);
BEGIN
  SELECT requests FROM decoding_requests
  INTO _curr_value
  WHERE id = _id;

  IF _curr_value IS NULL
  THEN
    INSERT INTO decoding_requests(ID, requests)
    VALUES(_id, _requests::BIT(32));
  ELSE
    UPDATE decoding_requests
    SET requests = _curr_value | _requests::BIT(32)
    WHERE ID=_id;
  END IF;
  RETURN;
END;
';


GRANT EXECUTE ON FUNCTION mark_decoding_request(BIGINT, INT) TO GROUP callrecgrp;
