INSERT INTO roles (id, role_name)
VALUES (default, 'ADMINISTRATOR');
INSERT INTO roles (id, role_name)
VALUES (default, 'CLIENT');

INSERT INTO users (id, blocked, email, first_name, last_name, patronymic, password, role_id)
VALUES ((SELECT nextval('users_id_seq')), false, 'admin@gmail.com', 'Admin', 'Admin', 'Admin',
        '$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS',
        (SELECT id FROM roles WHERE role_name = 'ADMINISTRATOR'));
