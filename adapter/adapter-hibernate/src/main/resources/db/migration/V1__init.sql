CREATE TABLE ROLE
(
    id   NUMBER PRIMARY KEY,
    name varchar2(50) NOT NULL UNIQUE
);
CREATE SEQUENCE sq_role_id START WITH 1 INCREMENT BY 1;

CREATE TABLE EMPLOYEE
(
    id            NUMBER PRIMARY KEY,
    login         VARCHAR2(255) NOT NULL UNIQUE,
    first_name    VARCHAR2(255) NOT NULL,
    patronymic    VARCHAR2(255),
    last_name     VARCHAR2(255) NOT NULL,
    hash_password VARCHAR2(255) NOT NULL,
    email         VARCHAR(255)  NOT NULL UNIQUE,
    state         VARCHAR2(10)  NOT NULL
);
CREATE SEQUENCE sq_employee_id START WITH 1 INCREMENT BY 1;

CREATE TABLE EMPLOYEE_ROLE
(
    employee_id NUMBER NOT NULL,
    role_id NUMBER NOT NULL
);

CREATE TABLE OFFICE
(
    id NUMBER PRIMARY KEY,
    name VARCHAR2(40) NOT NULL
);

CREATE SEQUENCE sq_office_id START WITH 1 INCREMENT BY 1;

CREATE TABLE VACATION
(
    id NUMBER PRIMARY KEY,
    employee_id NUMBER NOT NULL,
    start DATE,
    end DATE
);

CREATE SEQUENCE sq_vacation_id START WITH 1 INCREMENT BY 1;

INSERT INTO ROLE VALUES (next value for sq_role_id, 'ROLE_ADMIN');
INSERT INTO ROLE VALUES (next value for sq_role_id, 'ROLE_USER');
INSERT INTO ROLE VALUES (next value for sq_role_id, 'ROLE_HR');

-- Кодировка: BLOWFISH
-- LOGIN:PASSWORD:
-- admin:admin
-- hr:hr
-- amir:amir
-- olya:olya


INSERT INTO EMPLOYEE (id, login, first_name, patronymic, last_name, hash_password, email, state)
VALUES (next value for sq_employee_id, 'admin', 'Admin', null, ' ', '$2y$10$MfNi1OfcWHNvg2S/P5YtuuhRUhJAf8un/DcmuQk/gJZIK.TVf36p.', 'admin@gmail.com', 'ACTIVE'),
       (next value for sq_employee_id, 'hr', 'Hr', null, ' ', '$2y$10$6hAK0IIIJtC0Yjgi5KwWe.Dzwtntm.UoVgtIZeqDM6ol0YOFHDeUK', 'hr@gmail.com', 'ACTIVE'),
       (next value for sq_employee_id, 'amir', 'Амир', 'Ниязович', 'Ахатов', '$2y$10$9hrAQzlNGe5Ih6UxWsP8UuWkrxQDicRKquXZtqchVU9yLNa7EIgtG', 'Akhatov@mail.ru', 'ACTIVE'),
       (next value for sq_employee_id, 'olya', 'Оля', 'Вячеславовна', 'Сенькова', '$2y$10$aCBt4ChJSGE8OgkNGZYR3.VkYNAR0XC6gToM1AutfTFWnOJfdJKdS', 'Senkova@mail.ru', 'ACTIVE');

INSERT INTO EMPLOYEE_ROLE
VALUES (1, 1), (2, 3), (3, 2), (4, 2);

INSERT INTO OFFICE
VALUES (next value for sq_office_id, 'Москва'),
       (next value for sq_office_id, 'Нижний Новгород'),
       (next value for sq_office_id, 'Диптаун'),
       (next value for sq_office_id, 'Уфа');

