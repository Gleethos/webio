PRAGMA foreign_keys = ON;
--<#SPLIT#>--
DROP TABLE IF EXISTS temperatures;
--<#SPLIT#>--
CREATE TABLE temperatures(
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  value DOUBLE NOT NULL,
  created TEXT
);