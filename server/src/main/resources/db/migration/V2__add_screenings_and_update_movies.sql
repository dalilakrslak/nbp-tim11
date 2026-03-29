BEGIN;

-- Add language column to movies table
ALTER TABLE movies ADD COLUMN language VARCHAR(50) DEFAULT 'English';

-- Create screenings table
CREATE TABLE screenings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    movie_id UUID NOT NULL,
    start_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Add screenings for each movie
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + interval '1 day' + interval '12 hour'),
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + interval '1 day' + interval '15 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + interval '1 day' + interval '18 hour'),
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + interval '2 day' + interval '13 hour'),
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + interval '2 day' + interval '20 hour' + interval '15 minute');

INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + interval '1 day' + interval '11 hour'),
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + interval '1 day' + interval '14 hour'),
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + interval '1 day' + interval '17 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + interval '2 day' + interval '13 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + interval '2 day' + interval '19 hour' + interval '15 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + interval '1 day' + interval '10 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + interval '1 day' + interval '14 hour' + interval '15 minute'),
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + interval '1 day' + interval '18 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + interval '2 day' + interval '11 hour'),
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + interval '2 day' + interval '16 hour' + interval '30 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + interval '1 day' + interval '12 hour' + interval '15 minute'),
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + interval '1 day' + interval '16 hour'),
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + interval '1 day' + interval '19 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + interval '2 day' + interval '14 hour'),
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + interval '2 day' + interval '18 hour' + interval '45 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + interval '1 day' + interval '11 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + interval '1 day' + interval '15 hour'),
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + interval '1 day' + interval '19 hour'),
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + interval '2 day' + interval '12 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + interval '2 day' + interval '17 hour' + interval '45 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + interval '1 day' + interval '10 hour'),
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + interval '1 day' + interval '13 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + interval '1 day' + interval '17 hour'),
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + interval '2 day' + interval '11 hour' + interval '15 minute'),
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + interval '2 day' + interval '15 hour' + interval '45 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '12 hour'),
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '15 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '18 hour'),
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '1 day' + interval '13 hour'),
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '1 day' + interval '20 hour' + interval '15 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '11 hour'),
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '14 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '17 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '1 day' + interval '12 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '1 day' + interval '16 hour' + interval '15 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + interval '10 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + interval '13 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + interval '16 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + interval '1 day' + interval '11 hour' + interval '15 minute'),
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + interval '1 day' + interval '15 hour' + interval '45 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '12 hour' + interval '15 minute'),
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '15 hour'),
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '18 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '1 day' + interval '13 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '1 day' + interval '17 hour' + interval '15 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '11 hour' + interval '30 minute'),
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '14 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '18 hour'),
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '1 day' + interval '12 hour'),
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '1 day' + interval '16 hour' + interval '30 minute');


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '10 hour'),
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '13 hour' + interval '15 minute'),
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '17 hour'),
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '1 day' + interval '11 hour' + interval '45 minute'),
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '1 day' + interval '15 hour' + interval '30 minute');

COMMIT;
