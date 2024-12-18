--liquibase formatted sql

--changeset tonyayurina:1
ALTER TABLE users
ALTER COLUMN first_name DROP NOT NULL;

--changeset tonyayurina:2
ALTER TABLE users
    ALTER COLUMN last_name DROP NOT NULL;

--changeset tonyayurina:3
ALTER TABLE users
    ALTER COLUMN password DROP NOT NULL;