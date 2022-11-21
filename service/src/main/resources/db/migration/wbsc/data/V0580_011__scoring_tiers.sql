INSERT INTO wbsc.scoring_tiers (tier_name, description, scoring_tier_type, other_color)
VALUES ('Exact Scores', 'Percentage scores', 'EXACT_SCORES', 'NONE');

INSERT INTO wbsc.scoring_tiers (tier_name, description, scoring_tier_type, other_color)
VALUES ('No Scores', 'No score information', 'NO_SCORES', 'NONE');

INSERT INTO wbsc.scoring_tiers (tier_name, description, scoring_tier_type, other_color)
VALUES ('Color Scores', 'Color and label in place of percentage', 'COLOR_SCORES', 'NONE');

INSERT INTO wbsc.scoring_tier_values (value_to, color, display, scoring_tier_id)
VALUES (50, 'RED', NULL, (SELECT tier_id FROM wbsc.scoring_tiers where tier_name = 'Color Scores'));
INSERT INTO wbsc.scoring_tier_values (value_to, color, display, scoring_tier_id)
VALUES (75, 'YELLOW',NULL, (SELECT tier_id FROM wbsc.scoring_tiers where tier_name = 'Color Scores'));
INSERT INTO wbsc.scoring_tier_values (value_to, color, display, scoring_tier_id)
VALUES (100, 'GREEN', NULL, (SELECT tier_id FROM wbsc.scoring_tiers where tier_name = 'Color Scores'));
