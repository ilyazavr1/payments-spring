INSERT INTO roles (id, role_name) VALUES (default, 'ADMINISTRATOR');
INSERT INTO roles (id, role_name) VALUES (default, 'CLIENT');
--INSERT INTO roles (id, role_name) VALUES (default, 'ACCOUNTANT');

INSERT INTO country VALUES (default,'Ukraine');
INSERT INTO city VALUES (default,'Kyiv', (SELECT id FROM country WHERE country_name = 'Ukraine'));
INSERT INTO city VALUES (default,'Odessa', (SELECT id FROM country WHERE country_name = 'Ukraine'));
INSERT INTO city VALUES (default,'Lviv', (SELECT id FROM country WHERE country_name = 'Ukraine'));


INSERT INTO address VALUES (default, 54, 'Simi Prakhovykh', (SELECT id FROM city WHERE city_name = 'Kyiv'),(SELECT id FROM country WHERE country_name = 'Ukraine'));



-- pass: Qwerty12345
INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'admin@gmail.com', 'Admin','Admin', 'Admin','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'ADMINISTRATOR'));
--INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id) VALUES ((SELECT nextval('users_id_seq')), false, 'accountant@gmail.com', 'accountant','accountant', 'accountant','$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS', (SELECT id FROM roles WHERE role_name = 'ACCOUNTANT'));

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



INSERT INTO user_address VALUES (default, 1,1);
INSERT INTO user_address VALUES (default, 1,2);
INSERT INTO user_address VALUES (default, 1,3);
INSERT INTO user_address VALUES (default, 1,4);
INSERT INTO user_address VALUES (default, 1,5);
INSERT INTO user_address VALUES (default, 1,6);
INSERT INTO user_address VALUES (default, 1,7);
INSERT INTO user_address VALUES (default, 1,8);
INSERT INTO user_address VALUES (default, 1,10);
INSERT INTO user_address VALUES (default, 1,11);
INSERT INTO user_address VALUES (default, 1,12);