create table users
(
    uid      int auto_increment primary key,
    email    varchar(64) not null,
    username varchar(32) not null default '',
    password varchar(32) not null
)