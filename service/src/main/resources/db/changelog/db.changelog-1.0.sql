--liquibase formatted sql

--changeset tonyayurina:1
CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(128)        NOT NULL,
    last_name  VARCHAR(128)        NOT NULL,
    email      VARCHAR(128) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    phone      VARCHAR(20),
    user_role  VARCHAR(32)
);
--rollback DROP TABLE users;

--changeset tonyayurina:2
CREATE TABLE IF NOT EXISTS car
(
    id            SERIAL PRIMARY KEY,
    make          VARCHAR(128)       NOT NULL,
    model         VARCHAR(128)       NOT NULL,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    available  BOOLEAN
);
--rollback DROP TABLE car;

--changeset tonyayurina:3
CREATE TABLE IF NOT EXISTS user_car
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id),
    car_id  BIGINT REFERENCES car (id)
);
--rollback DROP TABLE user_car;

--changeset tonyayurina:4
CREATE TABLE IF NOT EXISTS ride
(
    id             SERIAL PRIMARY KEY,
    client_id      INT          NOT NULL,
    driver_id      INT          NOT NULL,
    start_location VARCHAR(255) NOT NULL,
    end_location   VARCHAR(255) NOT NULL,
    start_date     TIMESTAMP    NOT NULL,
    end_date       TIMESTAMP    NOT NULL,
    status         VARCHAR(100),
    cost           DECIMAL(10, 2),
    FOREIGN KEY (client_id) REFERENCES users (id),
    FOREIGN KEY (driver_id) REFERENCES users (id)
);
--rollback DROP TABLE ride;

--changeset tonyayurina:5
CREATE TABLE IF NOT EXISTS payment
(
    id             SERIAL PRIMARY KEY,
    ride_id        INT            NOT NULL,
    amount         DECIMAL(10, 2) NOT NULL,
    date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50)    NOT NULL,
    FOREIGN KEY (ride_id) REFERENCES ride (id)
);
--rollback DROP TABLE payment;

--changeset tonyayurina:6
CREATE TABLE IF NOT EXISTS review
(
    id          SERIAL PRIMARY KEY,
    ride_id     INT NOT NULL,
    client_id   INT NOT NULL,
    rating      INT,
    comment     TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ride_id) REFERENCES ride (id),
    FOREIGN KEY (client_id) REFERENCES users (id)
);
--rollback DROP TABLE review;