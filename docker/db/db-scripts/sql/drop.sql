ALTER SEQUENCE daily_user_user_id_seq RESTART WITH 1;
ALTER SEQUENCE task_task_id_seq RESTART WITH 1;
ALTER SEQUENCE schedule_schedule_id_seq RESTART WITH 1;
ALTER SEQUENCE user_type_user_type_id_seq RESTART WITH 1;
ALTER SEQUENCE task_type_task_type_id_seq RESTART WITH 1;
ALTER SEQUENCE task_notification_task_notification_id_seq RESTART WITH 1;
ALTER SEQUENCE binding_token_token_id_seq RESTART WITH 1;
DROP TABLE binding_token;
DROP TABLE task_notification;
DROP TABLE task;
DROP TABLE schedule;
DROP TABLE daily_user;
DROP TABLE user_type;
DROP TABLE task_type;

