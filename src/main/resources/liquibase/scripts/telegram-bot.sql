-- liquibase formatted sql
CREATE TABLE user (
                         id SERIAL PRIMARY KEY,
                         chatId INT,
                         catsOwnerName TEXT,
                         pet TEXT,
                         date timestamp );