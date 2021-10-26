create table contato (
    id serial not null primary key,
    nome varchar(120) not null,
    email varchar(200) not null unique,
    favorito boolean
);