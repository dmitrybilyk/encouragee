UPDATE callrec.speech_phrases
SET phrase_text = replace(replace(replace(phrase_text, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');

UPDATE callrec.speech_tags
SET tag_name = replace(replace(replace(tag_name, '&amp;', '&'), '&lt;', '<'), '&gt;', '>');