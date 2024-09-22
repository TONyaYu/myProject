-- Создание таблицы пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(128),
                       last_name VARCHAR(128),
                       email VARCHAR(128) UNIQUE,
                       password VARCHAR(255),
                       phone VARCHAR(20),
                       role VARCHAR(32),
                       status BOOLEAN
);

-- Создание таблицы автомобилей
CREATE TABLE car (
                     id BIGSERIAL PRIMARY KEY,
                     driver_id INT,
                     make VARCHAR(128),
                     model VARCHAR(128),
                     license_plate VARCHAR(20) UNIQUE,
                     status BOOLEAN,
                     FOREIGN KEY (driver_id) REFERENCES users(id)
);

-- Создание таблицы поездок
CREATE TABLE ride (
                      id SERIAL PRIMARY KEY,
                      client_id INT,
                      driver_id INT,
                      start_location VARCHAR(255),
                      end_location VARCHAR(255),
                      start_date TIMESTAMP,
                      end_date TIMESTAMP,
                      status VARCHAR(100),
                      cost DECIMAL(10, 2),
                      FOREIGN KEY (client_id) REFERENCES users(id),
                      FOREIGN KEY (driver_id) REFERENCES users(id)
);

-- Создание таблицы платежей
CREATE TABLE payment (
                         id BIGSERIAL PRIMARY KEY,
                         ride_id INT,
                         amount DECIMAL(10, 2),
                         date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         payment_method VARCHAR(50),
                         FOREIGN KEY (ride_id) REFERENCES Ride(id)
);

-- Создание таблицы отзывов
CREATE TABLE review (
                        id BIGSERIAL PRIMARY KEY,
                        ride_id INT,
                        client_id INT,
                        rating INT,
                        comment TEXT,
                        review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (ride_id) REFERENCES Ride(id),
                        FOREIGN KEY (client_id) REFERENCES users(id)
);

DROP TABLE users;