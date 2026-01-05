CREATE TABLE IF NOT EXISTS User_Type (
    user_type_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_type_name VARCHAR(100) CONSTRAINT UQ_User_Type_Name UNIQUE
);

CREATE TABLE IF NOT EXISTS Daily_User (
    user_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_login VARCHAR(100) CONSTRAINT Daily_User_Login UNIQUE,
    user_password VARCHAR(100) NOT NULL,
    user_first_name VARCHAR(100) NOT NULL,
    user_second_name VARCHAR(100) NOT NULL,
    user_patronymic VARCHAR(100),
    user_telegram_id BIGINT CONSTRAINT Daily_User_Telegram_Id UNIQUE,
    id_user_type INTEGER REFERENCES User_Type (user_type_id)
);

CREATE TABLE IF NOT EXISTS Schedule (
    schedule_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    schedule_name VARCHAR(100) NOT NULL,
    id_user INTEGER REFERENCES Daily_User (user_id),
    UNIQUE (schedule_name, id_user)
);

CREATE TABLE IF NOT EXISTS Task_Type (
    task_type_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    task_type_name VARCHAR(100) CONSTRAINT UQ_Task_Type_Name UNIQUE
);

CREATE TABLE IF NOT EXISTS Task (
    task_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    task_name VARCHAR(100) NOT NULL,
    task_description VARCHAR(100) NOT NULL,
    task_deadline TIMESTAMP NOT NULL,
    task_status BOOLEAN NOT NULL,
    id_schedule INTEGER REFERENCES Schedule (schedule_id),
    id_task_type INTEGER REFERENCES Task_Type (task_type_id),
    UNIQUE (task_name, task_description, id_schedule)
);

CREATE TABLE IF NOT EXISTS Task_Notification (
     task_notification_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     last_notified TIMESTAMP,
     id_task INTEGER UNIQUE NOT NULL REFERENCES Task (task_id)
);


CREATE TABLE IF NOT EXISTS Binding_Token (
    token_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    token_value varchar(64) NOT NULL CONSTRAINT UQ_Token_Value UNIQUE,
    id_user INTEGER REFERENCES Daily_User (user_id) NOT NULL CONSTRAINT UQ_Id_User UNIQUE
);