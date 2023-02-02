-- liquibase formatted sql

-- changeset nmavro:1

alter table info_message
    alter column text type varchar(1010) using text::varchar(1010);

-- changeset nmavro:2

INSERT INTO info_message
(tag, text)
VALUES
    ('/start', 'Привет! Я бот приюта для животных. Выбери меню из списка:'),
    ('/description', 'Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.' ||
                     'Для того, чтобы спасти жизнь, достаточно совсем немного...' ||
                     'Мы постарались создать сообщество для помощи людям, которые задумываются о том, чтобы забрать собаку или кошку домой.' ||
                     'Наш телеграмм-бот может ответить на вопросы людей о том, что нужно знать и уметь, чтобы забрать животное из приюта.'),
    ('/dogmenu', 'Здесь должно быть dogmenu, но пока что его нету'),
    ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету'),
    ('/volunteer', 'Хорошо, позову Волонтера (или нет)'),
    ('/addressandschedule', 'Адрес: г. Астана, ул. Тестовая, д.0. Часы работы: 10:00 - 20:00'),
    ('/safety', 'техника безопасности на территории приюта');

-- changeset irina:3
alter table info_message
    alter column text type text;

-- changeset irina:4
insert into info_message (tag, text)
values ('/generalmenu', 'Выберите команду:
/description - о нас
/addressandschedule - адрес и часы работы
/safety - техника безопасности на территории приюта
/callback - оставьте контактные данные, и мы Вам перезвоним
Не нашли нужную информацию? Для связи с волонтёром нажмите /volunteer');
