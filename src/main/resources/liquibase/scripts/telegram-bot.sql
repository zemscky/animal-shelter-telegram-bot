-- liquibase formatted sql

-- changeset irina:1
create table general_info
(
    id               int primary key,
    about            text not null default 'нет информации',
    schedule_address text not null default 'нет информации',
    directions       bytea,
    safety           text not null default 'нет информации'
);

-- changeset irina:2
insert into general_info (id, about, schedule_address, safety)
VALUES (1,
        'test info about shelter',
        'test schedule 10:00-20:00',
        'test safety rules: 1) first 2) second');
