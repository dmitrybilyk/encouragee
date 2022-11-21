ALTER TABLE wbsc.questforms
  ADD display_quick_notes boolean NOT NULL DEFAULT false;

ALTER TABLE wbsc.questforms
  ADD display_internal_comments boolean NOT NULL DEFAULT true;

ALTER TABLE wbsc.questforms
  ADD display_all_possible_answers boolean NOT NULL DEFAULT false;

ALTER TABLE wbsc.questforms
  ADD eco_printing boolean NOT NULL DEFAULT false;

-- Display by default all previous options
UPDATE 
	wbsc.questforms 
SET 
	display_quick_notes=true,
	display_internal_comments=true,
	display_all_possible_answers=true,
	eco_printing=true;
