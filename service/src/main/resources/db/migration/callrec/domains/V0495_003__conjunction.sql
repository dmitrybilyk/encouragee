--used in view_restriction_parts;


CREATE DOMAIN callrec.conjunction
  AS character varying
CONSTRAINT dom_conjunction CHECK (((VALUE)::text= ANY((ARRAY['AND'::character varying,'OR'::character varying,'END'::character varying])::text[])));
ALTER DOMAIN callrec.conjunction OWNER TO postgres;
