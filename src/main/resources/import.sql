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



INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'vlad@gmail.com', 'Vlad','Lyzogub', 'Olekseevych','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'artem@gmail.com', 'Arten','Vynyk', 'Nikitovych','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'nikita@gmail.com', 'Nikita','Gamaunov', 'Artomovych','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'sergey@gmail.com', 'Sergey','Masyna', 'Nikitovich','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'oleksandr@gmail.com', 'Oleksandr','Kucherenko', 'Andreevich','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'oleksey@gmail.com', 'Oleksii','Kucher', 'Petrovych','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'andry@gmail.com', 'Andry','Sikorskii', 'Myhailovych','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'maria@gmail.com', 'Maria','Krivenko', 'Antonova','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'yaroslav@gmail.com', 'Yaroslav','Masyna', 'Sergeevych','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'nikita01@gmail.com', 'Nikita','Lyzogub', 'Artomovich','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'CLIENT'));
