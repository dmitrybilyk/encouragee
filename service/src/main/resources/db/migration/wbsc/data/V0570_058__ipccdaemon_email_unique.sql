-- SC-8580: emails need to be unique for the encourage user importer
-- change the email, but only if it still was the original one

UPDATE wbsc.sc_users
SET email = 'james.doe@company.domain'
WHERE userid =
        (SELECT userid
         FROM wbsc.sc_users
         WHERE name = 'ipccimporterdaemon'
         AND email = 'jane.doe@company.domain'
        )
