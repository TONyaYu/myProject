--liquibase formatted sql

--changeset tonyayurina:1
ALTER TABLE users ALTER COLUMN password SET DEFAULT '{noop}123';