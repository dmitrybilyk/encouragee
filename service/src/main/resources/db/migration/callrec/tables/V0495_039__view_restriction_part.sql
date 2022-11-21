CREATE TABLE callrec.view_restriction_parts
(
  id integer NOT NULL,
  view_restrictionid integer,
  filterid integer,
  conjunction callrec.conjunction,
  rank smallint,
  CONSTRAINT view_filter_pk PRIMARY KEY (id),
  CONSTRAINT view_restriction_part_fk FOREIGN KEY (view_restrictionid)
      REFERENCES callrec.view_restrictions (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT viewfilterpart_filterid_fk FOREIGN KEY (filterid)
      REFERENCES callrec.filters (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE callrec.view_restriction_parts OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE callrec.view_restriction_parts TO callrecgrp;
COMMENT ON TABLE callrec.view_restriction_parts IS 'Conjunction is connection between this and following view_filter_part, rank defines order of view_filter_parts
filterid references to search filter';
