-- liquibase formatted sql

-- changeset nmavro:1

alter table info_message
    alter column text type varchar(1010) using text::varchar(1010);

-- changeset nmavro:2

INSERT INTO info_message
(tag, text)
VALUES
    ('/start', 'Привет! Я бот приюта для животных. Выбери раздел из списка:'),
    ('/aboutshelter', 'Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.' ||
                     'Для того, чтобы спасти жизнь, достаточно совсем немного...' ||
                     'Мы постарались создать сообщество для помощи людям, которые задумываются о том, чтобы забрать собаку или кошку домой.' ||
                     'Наш телеграмм-бот может ответить на вопросы людей о том, что нужно знать и уметь, чтобы забрать животное из приюта.'),
    ('/dogmenu', 'Здесь должно быть dogmenu, но пока что его нету'),
    ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету'),
    ('/volunteer', 'Хорошо, позову Волонтера (или нет)'),
    ('/addressandschedule', 'Адрес: г. Астана, ул. Тестовая, д.0. Часы работы: 10:00 - 20:00'),
    ('/safety', 'техника безопасности на территории приюта'),
    ('/description', 'Выберите раздел:');

-- changeset irina:3
alter table info_message
    alter column text type text;

-- changeset albert:4
INSERT INTO shelter (number, name, address, telephone_number, timetable, shelter_type)
VALUES
    ('1', 'Котики', 'г. Астана, ул. Зеленая, д. 1', '+7-999-99-99', 'Пн-Пт с 8-00 до 20-00', 0),
    ('2', 'Собачки', 'г. Астана, ул. Лесная, д. 2', '+7-888-88-88', 'Пн-Пт с 8-00 до 20-00', 1);

-- changeset zemscky:5
UPDATE public.info_message
SET text = 'Выберите раздел:'::varchar(255)
WHERE tag LIKE '/dogmenu' ESCAPE '#';

-- changeset zemscky:6
INSERT INTO public.info_message (tag, text)
VALUES ('/documents'::varchar(255),
        'При себе из документов обязательно иметь только паспорт. Документ обязательно фотографируют и вносят данные в базу данных. ' ||
        'Так как все собаки в приюте чипированы, необходимо внести в международную базу изменения о том, что собственник собаки сменился. ' ||
        'В самом учреждении также практикуют подписание договора, это внутренний документ, который остается в приюте. ' ||
        'Кроме того, составляется акт убытия — эти данные передают в Минсельхоз. Поэтому любое появление или передача животного в приюте фиксируется. ' ||
        'Так же вам предложат пройти что-то вроде собеседования, а в ходе общения уже вынесут решение, доверить вам животное или нет. ' ||
        'Для дополнительной консультации можете обратиться к волонтеру: /volunteer'::varchar(255)),
       ('/advice'::varchar(255),
        '1) Готовьтесь к тому, что собака не сразу привыкнет к новой обстановке. Дайте ей время, чтобы она смогла обследовать все углы и познакомиться со всеми членами семьи.' ||
        'В первый час лучше не слишком настойчиво к ней приставать. ' ||
        '2) Уже в первый день можно дать животному кличку и начать ее использовать. Собачки быстро привыкают к своему имени. ' ||
        '3) Чтобы пес привык к вам, в первые дни стоит проводить с ним больше времени. По этой причине лучше брать щенка на выходных.'::varchar(255)),
       ('/refusal'::varchar(255),
        '1) Животные НЕ ПЕРЕДАЮТСЯ лицам, пришедшим в состоянии алкогольного или наркотического опьянения.' ||
        '2) Животные НЕ ПЕРЕДАЮТСЯ гражданам, проживающим в арендуемом жилье. ' ||
        '3) Сотрудники приюта МОГУТ ОТКАЗАТЬ в передаче животного лицу, ранее утратившему животное, при этом, причины утраты животного признаны сотрудниками приюта не уважительными. ' ||
        'Данный перечень не является полным. Сотрудники приюта оставляют за собой ПРАВО ОТКАЗАТЬ в передаче животного и по иным причинам. ' ||
        'Всем желающим взять животное из приюта надо помнить о том, что передача животного из приюта новому владельцу – это НЕ ОБЯЗАННОСТЬ, а ПРАВО приюта.'::varchar(255));

-- changeset nmavro:7
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/start', 'Привет! Я бот приюта для животных. Выбери раздел из списка:');

-- changeset nmavro:8
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/dogmenu', 'Здесь должно быть dogmenu, но пока что его нету');

-- changeset nmavro:9
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету');

-- changeset nmavro:10
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/volunteer', 'Хорошо, позову Волонтера (или нет)');

-- changeset nmavro:11
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/addressandschedule', 'Адрес: г. Астана, ул. Тестовая, д.0. Часы работы: 10:00 - 20:00');

-- changeset nmavro:12
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/safety', 'техника безопасности на территории приюта');

-- changeset nmavro:13
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/description', 'Выберите раздел:');

-- changeset nmavro:14
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/passRegistration', 'У охраны собак такой номер +64684684');

-- changeset nmavro:15
INSERT INTO public.dog_info_message (tag, text)
VALUES ('/aboutshelter',
        'Мы приют для собак Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.Для того, чтобы спасти жизнь, достаточно совсем немного...');

-- changeset nmavro:16
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/dogmenu', 'Здесь должно быть dogmenu, но пока что его нету');

-- changeset nmavro:17
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету');

-- changeset nmavro:18
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/volunteer', 'Хорошо, позову Волонтера (или нет)');

-- changeset nmavro:19
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/addressandschedule', 'Адрес: г. Астана, ул. Тестовая, д.0. Часы работы: 10:00 - 20:00');

-- changeset nmavro:20
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/safety', 'техника безопасности на территории приюта');

-- changeset nmavro:21
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/description', 'Выберите раздел:');

-- changeset nmavro:22
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/passRegistration', 'У охраны собак такой номер +64684684');

-- changeset nmavro:23
INSERT INTO public.cat_info_message (tag, text)
VALUES ('/aboutshelter',
        'Мы приют для собак Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.Для того, чтобы спасти жизнь, достаточно совсем немного...');

-- changeset albert:24
INSERT INTO location_map (number, file_path, file_size, shelter_number)
VALUES
    ('1', '\shelters\shelter-map-1.jpg', 280887, '1'),
    ('2', '\shelters\shelter-map-2.jpg', 82963, '2');