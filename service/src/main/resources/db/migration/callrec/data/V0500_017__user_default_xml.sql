--CAL-5999
--SC-4101
UPDATE callrec.users_audit SET login='NoUser' WHERE login = '';



-- CAL-6104
ALTER TABLE callrec.users ALTER COLUMN config SET DEFAULT
'<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <SpecifiedConfiguration name="columns">
      <Group name="main_columns">
        <Value name="main_date">true</Value>
        <Value name="main_beginning">true</Value>
        <Value name="main_end">false</Value>
        <Value name="main_length">false</Value>
        <Value name="main_from">true</Value>
        <Value name="main_to">true</Value>
        <Value name="main_desc">true</Value>
      </Group>
      <Group name="lm_main_columns">
        <Value name="duration">true</Value>
        <Value name="callingNumber">true</Value>
        <Value name="calledNumber">true</Value>
        <Value name="recordStatus">true</Value>
      </Group>
   </SpecifiedConfiguration>
</Configuration>'::text;


