create table Cliente (
    id IDENTITY DEFAULT NOT NULL PRIMARY KEY,
    nome varchar(100) not null,
    data_nascimento date not null,
    sexo varchar(100) not null
);
