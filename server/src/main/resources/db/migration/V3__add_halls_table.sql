CREATE TABLE halls (
   id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
   name VARCHAR2(255) NOT NULL,
   venue_id RAW(16) NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_venue FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE
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
ALTER TABLE screenings ADD hall_id RAW(16);
ALTER TABLE screenings ADD CONSTRAINT fk_hall FOREIGN KEY (hall_id) REFERENCES halls(id) ON DELETE SET NULL;

-- Assign halls to existing screenings of Avatar: The Way of Water
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(18, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(13, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(20, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

-- Assign halls to existing screenings of Mickey 17
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(14, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Mickey 17')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(19, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

-- Assign halls to existing screenings of Oppenheimer
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(10, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(14, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(18, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(11, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Oppenheimer')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

-- Assign halls to existing screenings of Interstellar
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(16, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(19, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(14, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Interstellar')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(18, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

-- Assign halls to existing screenings of Once Upon a Time... in Hollywood
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(19, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

-- Assign halls to existing screenings of Pirates of the Caribbean
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(10, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(17, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')
  AND start_time = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');


-- Minecraft
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(12, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(18, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(13, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Minecraft')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(20, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

-- Captain America
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(11, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(14, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Multiplex 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Meeting Point'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

-- The Union
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(10, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Projekcijska Sala 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Kino Novi Grad Sarajevo'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Union')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

-- Gladiator II
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(15, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(18, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall B' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Art Hall A' AND venue_id = (SELECT id FROM venues WHERE name = 'Kinoteka Bosne i Hercegovine'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Gladiator II')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

-- Blink Twice
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(14, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(18, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Hall 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cineplex'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Blink Twice')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');

-- Wicked
UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(10, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 3' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(17, 'HOUR');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 1' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE');

UPDATE screenings
SET hall_id = (SELECT id FROM halls WHERE name = 'Ultimate Dvorana 2' AND venue_id = (SELECT id FROM venues WHERE name = 'Cinestar'))
WHERE movie_id = (SELECT id FROM movies WHERE title = 'Wicked')
  AND start_time = (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE');
