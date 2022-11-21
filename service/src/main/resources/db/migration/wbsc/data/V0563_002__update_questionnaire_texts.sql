update wbsc.QUESTFORMS set qformname = REPLACE(qformname, '&amp;','&');

update wbsc.QUESTIONGROUPS set groupname = REPLACE(groupname, '&amp;','&');
update wbsc.QUESTIONGROUPS set description = REPLACE(description, '&amp;','&');

update wbsc.QUESTIONS set question_text = REPLACE(question_text, '&amp;','&');
update wbsc.QUESTIONS set description = REPLACE(description, '&amp;','&');

update wbsc.ANSWERS set answer_text = REPLACE(answer_text, '&amp;','&');
update wbsc.ANSWERS set description = REPLACE(description, '&amp;','&');