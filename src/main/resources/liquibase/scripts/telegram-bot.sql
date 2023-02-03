-- liquibase formatted sql

-- changeset nmavro:1

alter table info_message
    alter column text type varchar(1010) using text::varchar(1010);

-- changeset nmavro:2

INSERT INTO info_message
(tag, text)
VALUES
    ('/start', 'Привет! Я бот приюта для животных'),
    ('/description', 'Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.' ||
                     'Для того, чтобы спасти жизнь, достаточно совсем немного...' ||
                     'Мы постарались создать сообщество для помощи людям, которые задумываются о том, чтобы забрать собаку или кошку домой.' ||
                     'Наш телеграмм-бот может ответить на вопросы людей о том, что нужно знать и уметь, чтобы забрать животное из приюта.'),
    ('/callback', 'Пожалуйста, оставьте свои контактные данный, чтобы мы могли с Вами связаться');

-- changeset irina:3
alter table info_message
    alter column text type text;

-- changeset albert:4
INSERT INTO shelter (number, address, name, telephone_number, timetable)
VALUES
    ('/contact1', 'Москва, ул. Зеленая, д. 1', 'Котики', '+7-999-99-99', 'Пн-Пт с 8-00 до 20-00'),
    ('/contact2', 'Ковров, ул. Лесная, д. 2', 'Собачки', '+7-888-88-88', 'Пн-Пт с 8-00 до 20-00'),
    ('/contact3', 'Можайск, ул. Ленина, д. 3', 'Жирафы', '+7-777-77-77', 'Пн-Пт с 8-00 до 20-00');