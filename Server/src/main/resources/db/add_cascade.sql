ALTER TABLE movies
    ADD CONSTRAINT fk_movies_operator
        FOREIGN KEY (operator_id)
            REFERENCES persons(id)
            ON DELETE CASCADE;
