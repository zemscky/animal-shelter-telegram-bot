-- liquibase formatted sql

-- changeset nmavro:1

alter table info_message
    alter column text type varchar(1010) using text::varchar(1010);

-- changeset nmavro:2

INSERT INTO info_message
(tag, text)
VALUES
    ('/start', 'Привет! Я бот приюта для животных. Выбери меню из списка:'),
    ('/generalmenu', 'Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.' ||
                     'Для того, чтобы спасти жизнь, достаточно совсем немного...' ||
                     'Мы постарались создать сообщество для помощи людям, которые задумываются о том, чтобы забрать собаку или кошку домой.' ||
                     'Наш телеграмм-бот может ответить на вопросы людей о том, что нужно знать и уметь, чтобы забрать животное из приюта.'),
    ('/dogmenu', 'Здесь должно быть dogmenu, но пока что его нету'),
    ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету'),
    ('/volunteer', 'Хорошо, позову Волонтера (или нет)');

-- changeset irina:3
alter table info_message
    alter column text type text;
