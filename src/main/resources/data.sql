INSERT INTO brand (id, brand_name) VALUES (1, 'BMW');
INSERT INTO brand (id, brand_name) VALUES (2, 'Mercedes');
INSERT INTO brand (id, brand_name) VALUES (3, 'Peugeot');
INSERT INTO brand (id, brand_name) VALUES (4, 'Seat');
INSERT INTO brand (id, brand_name) VALUES (5, 'Toyota');
INSERT INTO brand (id, brand_name) VALUES (6, 'Hyundai');
INSERT INTO brand (id, brand_name) VALUES (7, 'Ferari');

INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (1, 'IL', 100.00, 1, 'true');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (2, 'K', 200.00, 2, 'false');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (3, 'BL', 150.00, 3, 'true');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (4, 'IL', 100.00, 2, 'true');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (5, 'CR', 130.00, 5, 'false');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (6, 'K', 210.00, 4, 'false');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (7, 'K', 100.00, 1, 'false');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (8, 'BR', 220.00, 1, 'false');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (9, 'IL', 310.00, 2, 'true');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (10, 'K', 180.00, 6, 'true');
INSERT INTO car (id, reg_number, rental_price, brand_id, is_electric) VALUES (11, 'K', 210.00, 5, 'false');

INSERT INTO users (id, name, age, email, phone_number) VALUES (1, 'John', 32, 'john@gmail.com', '097865432');
INSERT INTO users (id, name, age, email, phone_number) VALUES (2, 'Maria', 23, 'maria@gmail.com', '097861232');
INSERT INTO users (id, name, age, email, phone_number) VALUES (3, 'Mohamed', 35, 'mohamed@gmail.com', '097005432');
INSERT INTO users (id, name, age, email, phone_number) VALUES (4, 'Ali', 28, 'ali@gmail.com', '097865452');
INSERT INTO users (id, name, age, email, phone_number) VALUES (5, 'Antonia', 18, 'antonia@gmail.com', '007865432');
INSERT INTO users (id, name, age, email, phone_number) VALUES (6, 'Ann', 45, 'ann@gmail.com', '097865402');

INSERT INTO car_booking (id, car_id, user_id, booking_time, is_canceled)
VALUES (1, 1, 1, NOW(), 'false');

SELECT setval('car_id_sequence', (SELECT COALESCE(MAX(id), 0) FROM car) + 1, false);
SELECT setval('brand_id_sequence', (SELECT COALESCE(MAX(id), 0) FROM brand) + 1, false);
SELECT setval('user_id_sequence', (SELECT COALESCE(MAX(id), 0) FROM users) + 1, false);
SELECT setval('booking_id_sequence', (SELECT COALESCE(MAX(id), 0) FROM car_booking) + 1, false);


