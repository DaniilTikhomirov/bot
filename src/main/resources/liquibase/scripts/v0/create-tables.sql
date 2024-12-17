-- liquibase formatted sql

-- changeset danil:v0-1-1
CREATE TABLE IF NOT EXISTS shelters(
    id bigserial PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    security_contact varchar(32),
    safety_recommendations TEXT,
    contact varchar(32),
    schedule_id bigint
);

-- changeset danil:v0-1-2
CREATE TABLE IF NOT EXISTS schedules(
    id bigserial PRIMARY KEY,
    shelters_id bigint,
    monday varchar(90),
    tuesday varchar(90),
    wednesday varchar(90),
    thursday varchar(90),
    friday varchar(90),
    saturday varchar(90),
    sunday varchar(90)
);

-- changeset danil:v0-1-3
CREATE TABLE IF NOT EXISTS volunteers(
    id bigserial PRIMARY KEY,
    shelters_id bigint,
    name varchar(255) NOT NULL,
    description TEXT,
    contact varchar(32) NOT NULL
);

-- changeset danil:v0-1-4
ALTER TABLE shelters ADD FOREIGN KEY (schedule_id) REFERENCES schedules(id);
ALTER TABLE schedules ADD FOREIGN KEY (shelters_id) REFERENCES shelters(id);
ALTER TABLE volunteers ADD FOREIGN KEY (shelters_id) REFERENCES shelters(id);

-- changeset danil:v0-1-5
ALTER TABLE shelters ADD COLUMN kind varchar(255);
ALTER TABLE shelters ADD CHECK (kind in ('cat', 'dog'));

-- changeset danil:v0-2-1
CREATE TABLE animals(
    id bigserial PRIMARY KEY,
    color varchar(255),
    description TEXT,
    kind varchar(255) NOT NULL CHECK ( kind IN ('dog', 'cat') ),
    shelters_id bigint NOT NULL
);


-- changeset danil:v0-2-2
CREATE TABLE habits(
    id bigserial PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    animal_id bigint

);

-- changeset danil:v0-2-3
ALTER TABLE animals ADD FOREIGN KEY (shelters_id) REFERENCES shelters(id);
ALTER TABLE habits ADD FOREIGN KEY (animal_id) REFERENCES animals(id);

-- changeset danil:v0-2-4
ALTER TABLE animals ALTER COLUMN shelters_id DROP NOT NULL;

-- changeset danil:v0-3-0
ALTER TABLE volunteers ADD COLUMN telegram_id bigint UNIQUE;

-- changeset danil:v0-3-1
ALTER TABLE animals ADD COLUMN review boolean DEFAULT FALSE;




