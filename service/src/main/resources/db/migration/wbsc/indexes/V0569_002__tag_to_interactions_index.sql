--SC-8457
CREATE INDEX sc_tag_to_interaction_couplsid_idx
   ON wbsc.tags_to_interactions
   USING btree (couple_sid);
