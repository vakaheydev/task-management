INSERT INTO user_type (user_type_name)
VALUES ('USER'),
       ('VIP'),
       ('ADMIN'),
       ('DEVELOPER');

INSERT INTO daily_user (user_login, user_password, user_first_name, user_second_name,
                        user_patronymic, user_telegram_id, id_user_type)
VALUES ('vaka', '$2a$10$IyiJtGoKbpWRt9PqN96pf.JjvCnVB7zXgvV9KDb7Z7M.5QFqUh32a', 'Иван', 'Новгородов', 'Андреевич', 1531192384, 4),
       ('retere', '$2a$10$3NtBOtOptorrVYJA1.5gQ.2hfINYxYEqourxFWPTISCde0bF52jtW', 'Павел', 'Новгородов', 'Андреевич', 5393306493, 1);

INSERT INTO daily_user (user_login, user_password, user_first_name, user_second_name,
                        user_patronymic, id_user_type)
VALUES ('aka', '$2a$10$LlvbV5ZNIzNrZLv4YEEx0OzK7cFF6RRCGvgIGm66FEYkXWObGgpSi', 'Анна', 'Новгородова', 'Андреевна', 1);

INSERT INTO schedule (schedule_name, id_user)
VALUES ('main', 1),
       ('main', 2),
       ('main', 3);

INSERT INTO task_type (task_type_name)
VALUES ('singular'),
       ('repetitive'),
       ('regular');

INSERT INTO task (task_name, task_description, task_deadline, task_status, id_schedule, id_task_type)
VALUES ('Прочитать книгу', 'Прочитать книгу Java Core', '2023-11-30', true, 1, 1),
       ('Разработать REST API', 'Полностью сделать REST API Vaka Daily', '2025-05-30', false, 1, 1),
       ('Прочитать книгу', 'Закончить книгу Pro Spring 6', '2024-07-31', true, 1, 1),
       ('Таска', 'Чем-то заняться', '2025-05-29', false, 2, 1),
       ('Таска', 'Чем-то заняться', '2025-05-28', false, 3, 1),
       ('Попоить жнецов', 'И так понятно', '2025-01-14', false, 1, 2),
       ('Белок никам', 'И так понятно', '2025-01-11', false, 1, 2),
       ('Попоить ников', 'И так понятно', '2025-01-09', false, 1, 2);

