UPDATE wbsc.questforms SET selected_agent_tier =
(SELECT tier_id FROM wbsc.scoring_tiers WHERE tier_name = 'Exact Scores');

UPDATE wbsc.questforms SET selected_evaluator_tier =
(SELECT tier_id FROM wbsc.scoring_tiers WHERE tier_name = 'Exact Scores');