-- CAL-14301
CREATE OR REPLACE FUNCTION callrec.del_req(BIGINT , INT)
  RETURNS INT
LANGUAGE plpgsql
AS '
DECLARE
  _id ALIAS FOR $1;
  _value ALIAS FOR $2;
  _curr_value BIT(32);
BEGIN
  SELECT
    requests
  FROM callrec.decoding_requests
  WHERE id = _id
  INTO _curr_value;

  IF _curr_value IS NULL
  THEN
    RAISE EXCEPTION ''No such request with id: %'', _id;
  END IF;

  _curr_value = _curr_value # _value :: BIT(32);

  IF _curr_value :: INT = 0
  THEN
    DELETE FROM callrec.decoding_requests
    WHERE id = _id;
    RETURN _curr_value :: INT;
  END IF;

  UPDATE callrec.decoding_requests
  SET requests = _curr_value
  WHERE id = _id;

  RETURN _curr_value :: INT;
END;
'