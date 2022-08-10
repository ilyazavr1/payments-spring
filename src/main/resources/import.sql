INSERT INTO roles (id, role_name) VALUES (default, 'ADMINISTRATOR');
INSERT INTO roles (id, role_name) VALUES (default, 'CLIENT');

-- CLIENT; email: vlad@gmail.com, pass: Qwerty12345
--INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'vlad@gmail.com', 'Vlad','Lizogub', 'Olekseevich','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
--INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'artem@gmail.com', 'Arten','Atr', 'Batyyko','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
--INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'nikita@gmail.com', 'NIkita','Gamaunov', 'Batyyko','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
--INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'sasha@gmail.com', 'Sasha','Saby', 'Batyyko','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
--INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'serega@gmail.com', 'Serega','Masina', 'Batyyko','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));

-- CLIENT; email: admin@gmail.com, pass: Qwerty12345
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'admin@gmail.com', 'Admin','Admin', 'Admin','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'ADMINISTRATOR'));