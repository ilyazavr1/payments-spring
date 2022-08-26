INSERT INTO roles (id, role_name)
VALUES (default, 'ADMINISTRATOR');
INSERT INTO roles (id, role_name)
VALUES (default, 'CLIENT');
INSERT INTO roles (id, role_name)
VALUES (default, 'ACCOUNTANT');

INSERT INTO country
VALUES (default, 'Ukraine');
INSERT INTO city
VALUES (default, 'Kyiv', (SELECT id FROM country WHERE country_name = 'Ukraine'));
INSERT INTO city
VALUES (default, 'Odessa', (SELECT id FROM country WHERE country_name = 'Ukraine'));
INSERT INTO city
VALUES (default, 'Lviv', (SELECT id FROM country WHERE country_name = 'Ukraine'));
INSERT INTO country
VALUES (default, 'Poland');
INSERT INTO city
VALUES (default, 'Krakov', (SELECT id FROM country WHERE country_name = 'Poland'));

INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id)
VALUES ((SELECT nextval('users_id_seq')), false, 'accountant@gmail.com', 'accountant', 'accountant', 'accountant',
        '$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS',
        (SELECT id FROM roles WHERE role_name = 'ACCOUNTANT'));


INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id)
VALUES ((SELECT nextval('users_id_seq')), false, 'admin@gmail.com', 'Admin', 'Admin', 'Admin',
        '$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS',
        (SELECT id FROM roles WHERE role_name = 'ADMINISTRATOR'));

