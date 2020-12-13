CREATE DATABASE ComputerShop;
USE ComputerShop;

DROP TABLE IF EXISTS Users;
CREATE TABLE Users
(
    Id        INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(280)        NOT NULL,
    LastName  VARCHAR(280)        NOT NULL,
    Email     VARCHAR(280)        NOT NULL UNIQUE,
    Type      VARCHAR(280)        NOT NULL,
    Password  VARCHAR(280)        NOT NULL,
    Verified  INTEGER             NOT NULL DEFAULT 0
);

DROP TABLE IF EXISTS Categories;
CREATE TABLE Categories
(
    Id   INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name VARCHAR(280)        NOT NULL UNIQUE
);

DROP TABLE IF EXISTS Products;
CREATE TABLE Products
(
    Id          INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name        VARCHAR(280)        NOT NULL,
    Description VARCHAR(280)        NOT NULL,
    Photo       VARCHAR(280)        NOT NULL,
    Category    INTEGER             NOT NULL,
    Quantity    INTEGER             NOT NULL,
    Price       FLOAT               NOT NULL
);

DROP TABLE IF EXISTS Carts;
CREATE TABLE Carts
(
    Id   INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    User INTEGER             NOT NULL
);

DROP TABLE IF EXISTS Commands;
CREATE TABLE Commands
(
    Id      INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    User    INTEGER             NOT NULL,
    Date    VARCHAR(280)        NOT NULL,
    Time    VARCHAR(280)        NOT NULL,
    Address VARCHAR(280)        NOT NULL,
    Status  VARCHAR(280)        NOT NULL
);

DROP TABLE IF EXISTS CommandItems;
CREATE TABLE CommandItems
(
    Id       INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Item     INTEGER             NOT NULL,
    Quantity INTEGER             NOT NULL,
    Command  INTEGER             NOT NULL
);

DROP TABLE IF EXISTS CartItems;
CREATE TABLE CartItems
(
    Id       INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Item     INTEGER             NOT NULL,
    Quantity INTEGER             NOT NULL,
    Cart     INTEGER             NOT NULL
);

INSERT INTO Users (Id, FirstName, LastName, Email, Type, Password, Verified)
VALUES (1, 'Computer', 'Shop', 'admin@gmail.com', 'admin', '21232f297a57a5a743894a0e4a801fc3', 1);