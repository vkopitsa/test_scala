# --- !Ups

CREATE TABLE clients (
    id bigserial NOT NULL,
    name varchar(100) NOT NULL,
    phone varchar(20),
    PRIMARY KEY (id)
);

# --- !Downs

drop table "clients" if exists;