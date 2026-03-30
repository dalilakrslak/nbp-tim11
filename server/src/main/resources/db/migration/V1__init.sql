-- ========================
-- Create Tables
-- ========================

CREATE TABLE locations (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    city VARCHAR2(255) NOT NULL,
    country VARCHAR2(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venues (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    street VARCHAR2(255) NOT NULL,
    image_url VARCHAR2(4000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    location_id RAW(16),
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE movies (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    title VARCHAR2(255),
    synopsis VARCHAR2(4000),
    duration INT,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'),
    pg_rating VARCHAR2(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE genres (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movie_genre (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    movie_id RAW(16) NOT NULL,
    genre_id RAW(16) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE movie_photos (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    url VARCHAR2(4000) NOT NULL,
    is_cover_image NUMBER(1,0) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    movie_id RAW(16) NOT NULL,
    CONSTRAINT fk_movie_photo FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- ========================
-- Populate data
-- ========================

-- Insert locations
INSERT INTO locations (city, country)
VALUES ('Sarajevo', 'Bosnia and Herzegovina');

COMMIT;

-- Insert venues
INSERT ALL
    INTO venues (name, street, image_url, location_id) VALUES ('Cineplex', 'Zmaja od Bosne 4, 71000 Sarajevo', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/Cineplexx1.jpg', (SELECT id FROM locations WHERE city = 'Sarajevo'))
INTO venues (name, street, image_url, location_id) VALUES ('Cinestar', 'Dzemala Bijedica St 160n, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/cinestar-1-318893.jpeg', (SELECT id FROM locations WHERE city = 'Sarajevo'))
INTO venues (name, street, image_url, location_id) VALUES ('Kino Meeting Point', 'Hamdije Kreševljakovića 13, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/kino-meeting-point-1589194182.jpg', (SELECT id FROM locations WHERE city = 'Sarajevo'))
INTO venues (name, street, image_url, location_id) VALUES ('Kino Novi Grad Sarajevo', 'Bulevar Meše Selimovića 97, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/Kino-Novi-Grad-Sarajevo.jpg', (SELECT id FROM locations WHERE city = 'Sarajevo'))
INTO venues (name, street, image_url, location_id) VALUES ('Kinoteka Bosne i Hercegovine', 'Alipašina 19, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/Kino-dvorana-Kinoteke-BiH.jpg', (SELECT id FROM locations WHERE city = 'Sarajevo'))
SELECT 1 FROM DUAL;

COMMIT;

-- Insert genres
INSERT ALL
    INTO genres (name) VALUES ('Action')
INTO genres (name) VALUES ('Comedy')
INTO genres (name) VALUES ('Thriller')
INTO genres (name) VALUES ('Sci-Fi')
INTO genres (name) VALUES ('Adventure')
INTO genres (name) VALUES ('Fantasy')
INTO genres (name) VALUES ('Drama')
INTO genres (name) VALUES ('History')
INTO genres (name) VALUES ('Mystery')
INTO genres (name) VALUES ('Romance')
INTO genres (name) VALUES ('Family')
SELECT 1 FROM DUAL;

COMMIT;

-- Insert movies
INSERT ALL
    INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Avatar: The Way of Water', 'Set more than a decade after the events of the first film, learn the story of the Sully family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure.', 120, TO_TIMESTAMP('2023-06-07', 'YYYY-MM-DD'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Mickey 17', 'Unlikely hero Mickey Barnes finds himself in the extraordinary circumstance of working for an employer who demands the ultimate commitment to the job… to die, for a living.', 137, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Oppenheimer', 'The story of J. Robert Oppenheimer''s role in the development of the atomic bomb during World War II.', 181, TO_TIMESTAMP('2023-08-20', 'YYYY-MM-DD'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Interstellar', 'The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.', 169, TO_TIMESTAMP('2014-11-06', 'YYYY-MM-DD'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Once Upon a Time... in Hollywood', 'Los Angeles, 1969. TV star Rick Dalton, a struggling actor specializing in westerns, and stuntman Cliff Booth, his best friend, try to survive in a constantly changing movie industry. Dalton is the neighbor of the young and promising actress and model Sharon Tate, who has just married the prestigious Polish director Roman Polanski…', 162, TO_TIMESTAMP('2019-07-26', 'YYYY-MM-DD'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Pirates of the Caribbean: The Curse of the Black Pearl', 'After Port Royal is attacked and pillaged by a mysterious pirate crew, capturing the governor''s daughter Elizabeth Swann in the process, William Turner asks free-willing pirate Jack Sparrow to help him locate the crew''s ship—The Black Pearl—so that he can rescue the woman he loves.', 162, TO_TIMESTAMP('2003-07-09', 'YYYY-MM-DD'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Minecraft', 'Four misfits find themselves struggling with ordinary problems when they are suddenly pulled through a mysterious portal into the Overworld: a bizarre, cubic wonderland that thrives on imagination. To get back home, they''ll have to master this world while embarking on a magical quest with an unexpected, expert crafter, Steve.', 120, CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(60, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Captain America: Brave New World', 'After meeting with newly elected U.S. President Thaddeus Ross, Sam finds himself in the middle of an international incident. He must discover the reason behind a nefarious global plot before the true mastermind has the entire world seeing red.', 120, CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(60, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('The Union', 'A New Jersey construction worker goes from regular guy to aspiring spy when his long-lost high school sweetheart recruits him for an espionage mission.', 109, CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(60, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Gladiator II', 'Years after witnessing the death of the revered hero Maximus at the hands of his uncle, Lucius is forced to enter the Colosseum after his home is conquered by the tyrannical Emperors who now lead Rome with an iron fist. With rage in his heart and the future of the Empire at stake, Lucius must look to his past to find strength and honor to return the glory of Rome to its people.', 148, CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(60, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Blink Twice', 'When tech billionaire Slater King meets cocktail waitress Frida at his fundraising gala, he invites her to join him and his friends on a dream vacation on his private island. But despite the epic setting, beautiful people, ever-flowing champagne, and late-night dance parties, Frida can sense that there''s something sinister hiding beneath the island''s lush façade.', 102, CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(60, 'DAY'), 'PG_13')
INTO movies (title, synopsis, duration, start_date, end_date, pg_rating) VALUES ('Wicked', 'In the land of Oz, ostracized and misunderstood green-skinned Elphaba is forced to share a room with the popular aristocrat Glinda at Shiz University, and the two''s unlikely friendship is tested as they begin to fulfill their respective destinies as Glinda the Good and the Wicked Witch of the West.', 162, CURRENT_TIMESTAMP + NUMTODSINTERVAL(30, 'DAY'), CURRENT_TIMESTAMP + NUMTODSINTERVAL(60, 'DAY'), 'PG')
SELECT 1 FROM DUAL;

COMMIT;

-- Insert movie-genre relationships
BEGIN
    INSERT INTO movie_genre (movie_id, genre_id)
    SELECT m.id, g.id
    FROM movies m, genres g
    WHERE (m.title = 'Avatar: The Way of Water' AND g.name IN ('Sci-Fi', 'Adventure', 'Action'))
       OR (m.title = 'Mickey 17' AND g.name IN ('Sci-Fi', 'Thriller'))
       OR (m.title = 'Oppenheimer' AND g.name IN ('Drama', 'History'))
       OR (m.title = 'Interstellar' AND g.name IN ('Adventure', 'Drama', 'Sci-Fi'))
       OR (m.title = 'Once Upon a Time... in Hollywood' AND g.name IN ('Comedy', 'Drama', 'Thriller'))
       OR (m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND g.name IN ('Adventure', 'Fantasy', 'Action'))
       OR (m.title = 'Minecraft' AND g.name IN ('Family', 'Comedy', 'Adventure', 'Fantasy'))
       OR (m.title = 'Captain America: Brave New World' AND g.name IN ('Action', 'Thriller', 'Sci-Fi'))
       OR (m.title = 'The Union' AND g.name IN ('Action', 'Comedy'))
       OR (m.title = 'Gladiator II' AND g.name IN ('Action', 'Adventure', 'Drama'))
       OR (m.title = 'Blink Twice' AND g.name IN ('Mystery', 'Thriller'))
       OR (m.title = 'Wicked' AND g.name IN ('Drama', 'Romance', 'Fantasy'));

    COMMIT;
END;
/

-- Insert movie photos
INSERT ALL
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Avatar/avatar-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Avatar/avatar2.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Mickey17/mickey-17-k5.jpg', 1, (SELECT id FROM movies WHERE title = 'Mickey 17'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Oppenheimer/oppeneimer-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Interstellar/interstellar-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Interstellar'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlackPearl/blackpearl-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/OnceUpon/onceupon-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Minecraft/minecraft-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Minecraft'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/CaptainAmericaBraveNewWorld/captainamerica-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/TheUnion/theunion-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'The Union'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/GladiatorII/gladiator2-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Gladiator II'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlinkTwice/blinktwice-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Blink Twice'))
INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Wicked/wicked-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Wicked'))
SELECT 1 FROM DUAL;

COMMIT;
