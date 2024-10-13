-- Создание таблицы пользователей
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(128) NOT NULL,
                       last_name VARCHAR(128) NOT NULL,
                       email VARCHAR(128) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(20),
                       role VARCHAR(32),
                       status BOOLEAN
);

-- Создание таблицы автомобилей
CREATE TABLE car (
                     id SERIAL PRIMARY KEY,
                     driver_id INT NOT NULL,
                     make VARCHAR(128) NOT NULL,
                     model VARCHAR(128) NOT NULL,
                     license_plate VARCHAR(20) UNIQUE NOT NULL,
                     is_available BOOLEAN,
                     FOREIGN KEY (driver_id) REFERENCES users(id)
);

-- Создание таблицы поездок
CREATE TABLE ride (
                      id SERIAL PRIMARY KEY,
                      client_id INT NOT NULL,
                      driver_id INT NOT NULL,
                      start_location VARCHAR(255) NOT NULL,
                      end_location VARCHAR(255) NOT NULL,
                      start_date TIMESTAMP NOT NULL,
                      end_date TIMESTAMP NOT NULL,
                      status VARCHAR(100),
                      cost DECIMAL(10, 2),
                      FOREIGN KEY (client_id) REFERENCES users(id),
                      FOREIGN KEY (driver_id) REFERENCES users(id)
);

-- Создание таблицы платежей
CREATE TABLE payment (
                         id SERIAL PRIMARY KEY,
                         ride_id INT NOT NULL,
                         amount DECIMAL(10, 2) NOT NULL,
                         date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         payment_method VARCHAR(50) NOT NULL,
                         FOREIGN KEY (ride_id) REFERENCES ride(id)
);

-- Создание таблицы отзывов
CREATE TABLE review (
                        id SERIAL PRIMARY KEY,
                        ride_id INT NOT NULL,
                        client_id INT NOT NULL,
                        rating INT,
                        comment TEXT,
                        review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (ride_id) REFERENCES ride(id),
                        FOREIGN KEY (client_id) REFERENCES users(id)
);

DROP TABLE users;
DROP TABLE car;
DROP TABLE ride;
DROP TABLE payment;
DROP TABLE review;