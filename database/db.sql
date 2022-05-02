CREATE DATABASE quarkus-rede-social;

CREATE TABLE USUARIOS(
	id bigserial not null primary key,
	name varchar(100) not null,
	idade integer not null
);

CREATE TABLE POSTAGENS (
    id bigserial not null primary key,
    postagem_text varchar(150) not null,
    datetime timestamp not null,
    usuarios_id bigint not null references usuarios(id)
);

CREATE TABLE SEGUIDORES(
	id bigserial not null primary key,
	usuario_id bigint not null references USUARIOS(id),
	seguidor_id bigint not null references USUARIOS(id)
);