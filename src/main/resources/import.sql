insert into users (id, email, name, surname) values (nextval('users_seq'), 'prova@gmail.com', 'prova', 'prova');
insert into credentials (id, user_id, password, role, username) values (nextval('credentials_seq'), currval('users_seq'), '$2a$10$NHVH3y2/Ue0uHTYLz.6e9OOR94x3NF6dEtfIgO8NewSFsNVaqwzc2', 'DEFAULT', 'prova');

insert into users (id, email, name, surname) values (nextval('users_seq'), 'admin@gmail.com', 'admin_nome', 'admin_cognome');
insert into credentials (id, user_id, password, role, username) values (nextval('credentials_seq'), currval('users_seq'), '$2a$10$vBNf76nub/flAqqYCjExhOWS8D/XyPCqcJcWnq7wKeUnoknVDlBI6', 'ADMIN', 'admin');

