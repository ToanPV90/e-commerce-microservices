INSERT INTO users(id, username, password) VALUES
    ('748a11b7-6dee-49d8-9485-1441c9ce0e6e',
    'demo',
    '$2y$10$HUF6CnifEaXN7KfFZCjlIu5H3frotYLlQ5plPlSJLBTiXLohuHh/i') -- demo-password
    ON CONFLICT DO NOTHING;

INSERT INTO users(id, username, password) VALUES
    ('c24f0887-a071-4918-b17d-6a73d1356224',
    'admin',
    '$2y$10$r.QxpK.8/fJdVdqTxDr7xOTs994DysK5KNzFzPwkNsw9lWRhg.RJG') -- admin-password
    ON CONFLICT DO NOTHING;

INSERT INTO roles(id, name) VALUES
    (1, 'USER'),
    (2, 'ADMIN')
    ON CONFLICT DO NOTHING;

INSERT INTO users_roles(user_fk, role_fk) VALUES
    ('748a11b7-6dee-49d8-9485-1441c9ce0e6e', 1),
    ('c24f0887-a071-4918-b17d-6a73d1356224', 1),
    ('c24f0887-a071-4918-b17d-6a73d1356224', 2)
    ON CONFLICT DO NOTHING;

SELECT setval('roles_id_seq', 3);