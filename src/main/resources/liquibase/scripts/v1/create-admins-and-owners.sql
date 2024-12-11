-- liquibase formatted sql


-- changeset danil:v1-1-1
CREATE TABLE administrator(
    id serial PRIMARY KEY,
    name varchar(255),
    telegram_id bigint
);

-- changeset danil:v1-2-1
CREATE TABLE owners_shelters(
    id bigserial PRIMARY KEY,
    name varchar(255),
    telegram_id bigint
);

-- changeset danil:v1-2-2
CREATE TABLE owners_many_to_many_shelters(
    id bigserial PRIMARY KEY,
    shelters_id bigint,
    owners_shelters_id bigint
);

-- changeset danil:v1-2-3
ALTER TABLE owners_many_to_many_shelters ADD FOREIGN KEY (shelters_id) REFERENCES shelters(id);
ALTER TABLE owners_many_to_many_shelters ADD FOREIGN KEY (owners_shelters_id) REFERENCES owners_shelters(id);
