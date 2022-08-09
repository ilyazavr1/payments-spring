create table card_unblock_requests
(
    id        int8 generated by default as identity,
    processed boolean default false,
    card_id   int8 not null,
    user_id   int8 not null,
    primary key (id)
);

create table cards
(
    id                  int8 generated by default as identity,
    blocked             boolean default false,
    money               int4,
    name                varchar(255) not null,
    number              varchar(255) not null,
    under_consideration boolean default false,
    user_id             int8         not null,
    primary key (id)
);

create table payments
(
    id                  int8 generated by default as identity,
    balance             int4      not null,
    balance_destination int4      not null,
    creation_timestamp  timestamp not null,
    is_send             boolean default false,
    money               int4      not null,
    card_destination_id int8      not null,
    card_sender_id      int8      not null,
    user_destination_id int8      not null,
    user_id             int8      not null,
    primary key (id)
);

create table roles
(
    id        int8 generated by default as identity,
    role_name varchar(255) not null,
    primary key (id)
);

create table users
(
    id         int8 generated by default as identity,
    blocked    boolean default false,
    email      varchar(255) not null UNIQUE,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    password   varchar(255) not null,
    patronymic varchar(255) not null,
    role_id    int8         not null,
    primary key (id)
);


alter table if exists card_unblock_requests
    add constraint FKhwcjbcqe8aul1qns1qvf9jvan
        foreign key (card_id)
            references cards;


alter table if exists card_unblock_requests
    add constraint FKpf9icb9hyla2uwv0udxoafqkh
        foreign key (user_id)
            references users;

alter table if exists cards
    add constraint FKcmanafgwbibfijy2o5isfk3d5
        foreign key (user_id)
            references users;

alter table if exists payments
    add constraint FK1r9mb705e2jgaigqvxheur36k
        foreign key (card_destination_id)
            references cards;

alter table if exists payments
    add constraint FKrqromph6vp7nohlkur9vyl1oe
        foreign key (card_sender_id)
            references cards;

alter table if exists payments
    add constraint FKg6ydlr83utt2y3dxi0a4fift0
        foreign key (user_destination_id)
            references users;

alter table if exists payments
    add constraint FKj94hgy9v5fw1munb90tar2eje
        foreign key (user_id)
            references users;

alter table if exists users
    add constraint FKp56c1712k691lhsyewcssf40f
        foreign key (role_id)
            references roles;

