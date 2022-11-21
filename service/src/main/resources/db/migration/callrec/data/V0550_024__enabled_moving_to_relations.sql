--SC-6122
UPDATE callrec.tag_to_phrases set enabled = (select enabled from callrec.speech_phrases WHERE
callrec.speech_phrases.phraseid = callrec.tag_to_phrases.phraseid);