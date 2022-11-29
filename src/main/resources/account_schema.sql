-- CREATE DATABASE bank;
-- CREATE SCHEMA bank;
-- DROP USER 'banker'@'localhost';
-- CREATE USER 'banker'@'localhost' IDENTIFIED BY 'mysupersecretpassword';
-- GRANT ALL PRIVILEGES ON bank.* TO 'banker'@'localhost';
USE bank;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS account;

CREATE TABLE `account` (
  `Account` varchar(14) NOT NULL,
  `Owner` char(128) NOT NULL,
  `Balance` decimal(13,2) NOT NULL DEFAULT '0.00',
  `TransactionDate` datetime NOT NULL DEFAULT (curdate()),
  PRIMARY KEY (`Account`)
);
