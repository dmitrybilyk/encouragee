UPDATE wbsc.answers
SET answer_text = replace(replace(replace(answer_text, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.appoptions
SET value = replace(replace(replace(value, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.audit
SET parameters = replace(replace(replace(parameters, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.auditreason
SET value = replace(replace(replace(value, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.callwrapups
SET key = replace(replace(replace(key, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.categories
SET category_name = replace(replace(replace(category_name, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.ccgroups
SET ccgroupname = replace(replace(replace(ccgroupname, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.criteria
SET description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.data_export
SET name = replace(replace(replace(name, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.evaluations
SET comments = replace(replace(replace(replace(replace(comments, '&#39;', ''''), '&quot;', '"'), '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
feedback_improve = replace(replace(replace(replace(replace(feedback_improve, '&#39;', ''''), '&quot;', '"'), '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
feedback_maintain = replace(replace(replace(replace(replace(feedback_maintain, '&#39;', ''''), '&quot;', '"'), '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.interaction_tags
SET tagtext = replace(replace(replace(tagtext, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.interaction_types
SET name = replace(replace(replace(name, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.messagebox
SET text = replace(replace(replace(text, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.questforms
SET qformname = replace(replace(replace(qformname, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.questiongroups
SET groupname = replace(replace(replace(groupname, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.questions
SET question_text = replace(replace(replace(question_text, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.roles
SET name = replace(replace(replace(name, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.sc_users
SET name = replace(replace(replace(name, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
surname = replace(replace(replace(surname, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.scoring_tier_values
SET display = replace(replace(replace(display, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.scoring_tiers
SET tier_name = replace(replace(replace(tier_name, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.search_templates
SET templatename = replace(replace(replace(templatename, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.subevaluation
SET ticket_number = replace(replace(replace(ticket_number, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
mail_number = replace(replace(replace(mail_number, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
feedback_improve = replace(replace(replace(replace(replace(feedback_improve, '&#39;', ''''), '&quot;', '"'), '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
feedback_maintain = replace(replace(replace(replace(replace(feedback_maintain, '&#39;', ''''), '&quot;', '"'), '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
internal_note = replace(replace(replace(replace(replace(internal_note, '&#39;', ''''), '&quot;', '"'), '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
fast_note = replace(replace(replace(replace(replace(fast_note, '&#39;', ''''), '&quot;', '"'), '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.templates
SET templatename = replace(replace(replace(templatename, '&amp;', '&'), '&lt;', '<'), '&gt;', '>'),
description = replace(replace(replace(description, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE wbsc.user_settings
SET value = replace(replace(replace(value, '&amp;', '&'), '&lt;', '<'), '&gt;', '>')
WHERE key = 'DASHBOARD_CONFIG';
