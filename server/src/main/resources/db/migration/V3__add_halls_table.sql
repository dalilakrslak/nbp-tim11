BEGIN;

CREATE TABLE halls (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   name TEXT NOT NULL,
   venue_id UUID NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_venue FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO halls (name, venue_id)
SELECT 'Hall 1', id FROM venues WHERE name = 'Cineplex';

INSERT INTO halls (name, venue_id)
SELECT 'Hall 2', id FROM venues WHERE name = 'Cineplex';

INSERT INTO halls (name, venue_id)
SELECT 'Hall 3', id FROM venues WHERE name = 'Cineplex';


INSERT INTO halls (name, venue_id)
SELECT 'Ultimate Dvorana 1', id FROM venues WHERE name = 'Cinestar';

INSERT INTO halls (name, venue_id)
SELECT 'Ultimate Dvorana 2', id FROM venues WHERE name = 'Cinestar';

INSERT INTO halls (name, venue_id)
SELECT 'Ultimate Dvorana 3', id FROM venues WHERE name = 'Cinestar';


INSERT INTO halls (name, venue_id)
SELECT 'Multiplex 1', id FROM venues WHERE name = 'Kino Meeting Point';

INSERT INTO halls (name, venue_id)
SELECT 'Multiplex 2', id FROM venues WHERE name = 'Kino Meeting Point';


INSERT INTO halls (name, venue_id)
SELECT 'Projekcijska Sala 1', id FROM venues WHERE name = 'Kino Novi Grad Sarajevo';

INSERT INTO halls (name, venue_id)
SELECT 'Projekcijska Sala 2', id FROM venues WHERE name = 'Kino Novi Grad Sarajevo';


INSERT INTO halls (name, venue_id)
SELECT 'Art Hall A', id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine';

INSERT INTO halls (name, venue_id)
SELECT 'Art Hall B', id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine';

-- Add the hall_id column to the screenings table
ALTER TABLE screenings ADD COLUMN hall_id UUID;
ALTER TABLE screenings ADD CONSTRAINT fk_hall FOREIGN KEY (hall_id) REFERENCES halls(id) ON DELETE SET NULL ON UPDATE CASCADE;

-- Assign halls to existing screenings of Avatar: The Way of Water
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '12 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '15 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '18 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '13 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '20 hour' + interval '15 minute';

-- Assign halls to existing screenings of Mickey 17
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '11 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '14 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '17 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '13 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '19 hour' + interval '15 minute';

-- Assign halls to existing screenings of Oppenheimer
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '10 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '14 hour' + interval '15 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '18 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '11 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '16 hour' + interval '30 minute';

-- Assign halls to existing screenings of Interstellar
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '12 hour' + interval '15 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '16 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '19 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '14 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '18 hour' + interval '45 minute';

-- Assign halls to existing screenings of Once Upon a Time... in Hollywood
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '11 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '15 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '19 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '12 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '17 hour' + interval '45 minute';

-- Assign halls to existing screenings of Pirates of the Caribbean
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '10 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '13 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + interval '1 day' + interval '17 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '11 hour' + interval '15 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + interval '2 day' + interval '15 hour' + interval '45 minute';


-- Minecraft
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '12 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '15 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '18 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '1 day' + interval '13 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + interval '1 day' + interval '20 hour' + interval '15 minute';

-- Captain America
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '11 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '14 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '17 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '1 day' + interval '12 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + interval '1 day' + interval '16 hour' + interval '15 minute';

-- The Union
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + interval '10 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + interval '13 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + interval '16 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + interval '1 day' + interval '11 hour' + interval '15 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + interval '1 day' + interval '15 hour' + interval '45 minute';

-- Gladiator II
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '12 hour' + interval '15 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '15 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '18 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '1 day' + interval '13 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + interval '1 day' + interval '17 hour' + interval '15 minute';

-- Blink Twice
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '11 hour' + interval '30 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '14 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '18 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '1 day' + interval '12 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + interval '1 day' + interval '16 hour' + interval '30 minute';

-- Wicked
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '10 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '13 hour' + interval '15 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '17 hour';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '1 day' + interval '11 hour' + interval '45 minute';

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + interval '1 day' + interval '15 hour' + interval '30 minute';

COMMIT;
