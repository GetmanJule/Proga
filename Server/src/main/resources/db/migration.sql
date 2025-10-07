-- Таблица пользователей
CREATE TABLE users (
                       login TEXT PRIMARY KEY,
                       password_hash TEXT NOT NULL
);

-- Таблица Person (оператор)
CREATE TABLE persons (
                         id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         passport_id TEXT UNIQUE NOT NULL,
                         eye_color TEXT,
                         nationality TEXT NOT NULL,
                         loc_x REAL,
                         loc_y DOUBLE PRECISION,
                         loc_name TEXT
);

-- Таблица Movie
CREATE SEQUENCE movie_seq START 1;

CREATE TABLE movies (
                        id BIGINT PRIMARY KEY DEFAULT nextval('movie_seq'),
                        name TEXT NOT NULL,
                        coord_x REAL NOT NULL,
                        coord_y BIGINT NOT NULL,
                        creation_date TIMESTAMP NOT NULL DEFAULT NOW(),
                        oscars_count BIGINT NOT NULL CHECK (oscars_count > 0),
                        budget REAL NOT NULL CHECK (budget > 0),
                        usa_box_office DOUBLE PRECISION NOT NULL CHECK (usa_box_office > 0),
                        mpaa_rating TEXT NOT NULL,
                        operator_id BIGINT REFERENCES persons(id),
                        owner_login TEXT REFERENCES users(login)
);
