ALTER TABLE wbsc.templates
	ADD COLUMN last_run timestamp with time zone, --last execution time
  ADD COLUMN next_run timestamp with time zone, --next execution time
  ADD COLUMN recurring_type wbsc.data_export_recurring_domain, --rule for calculation of next run
  ADD COLUMN enabled boolean default false;
