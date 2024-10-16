-- IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'profiledb')
-- BEGIN
--     CREATE DATABASE [profiledb];
-- END;

-- IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'accountdb')
-- BEGIN
--     CREATE DATABASE [accountdb];
-- END;

CREATE DATABASE IF NOT EXISTS profiledb;
CREATE DATABASE IF NOT EXISTS accountdb;