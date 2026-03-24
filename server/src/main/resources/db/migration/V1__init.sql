BEGIN;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE locations (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   city TEXT NOT NULL,
   country TEXT NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venues (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    street TEXT NOT NULL,
    image_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    location_id UUID,
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE movies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT,
    synopsis TEXT,
    duration INT,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP + interval '30 days',
    pg_rating VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE genres (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movie_genre (
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     movie_id UUID NOT NULL,
     genre_id UUID NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE,
     CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE movie_photos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    url TEXT NOT NULL,
    is_cover_image BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    movie_id UUID NOT NULL,
    CONSTRAINT fk_movie_photo FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================
-- Populate data
-- ========================

INSERT INTO locations (city, country)
VALUES ('Sarajevo', 'Bosnia and Herzegovina');

INSERT INTO venues (name, street, image_url, location_id)
VALUES

    ('Cineplex', 'Zmaja od Bosne 4, 71000 Sarajevo', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/Cineplexx1.jpg',
    (SELECT id FROM locations WHERE city = 'Sarajevo')),

    ('Cinestar', 'Dzemala Bijedica St 160n, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/cinestar-1-318893.jpeg',
    (SELECT id FROM locations WHERE city = 'Sarajevo')),

    ('Kino Meeting Point', 'Hamdije Kreševljakovića 13, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/kino-meeting-point-1589194182.jpg',
    (SELECT id FROM locations WHERE city = 'Sarajevo')),

    ('Kino Novi Grad Sarajevo', 'Bulevar Meše Selimovića 97, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/Kino-Novi-Grad-Sarajevo.jpg',
    (SELECT id FROM locations WHERE city = 'Sarajevo')),

    ('Kinoteka Bosne i Hercegovine', 'Alipašina 19, Sarajevo 71000', 'https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/venues/Kino-dvorana-Kinoteke-BiH.jpg',
    (SELECT id FROM locations WHERE city = 'Sarajevo'));


INSERT INTO genres (name)
VALUES
    ('Action'),
    ('Comedy'),
    ('Thriller'),
    ('Sci-Fi'),
    ('Adventure'),
    ('Fantasy'),
    ('Drama'),
    ('History'),
    ('Mystery'),
    ('Romance'),
    ('Family');

INSERT INTO movies (title, synopsis, duration, start_date, end_date, pg_rating)
VALUES
    ('Avatar: The Way of Water', 'Set more than a decade after the events of the first film, learn the story of the Sully family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure.', 120, '2023-06-07', CURRENT_DATE + INTERVAL '30 days', 'PG_13'),
    ('Mickey 17', 'Unlikely hero Mickey Barnes finds himself in the extraordinary circumstance of working for an employer who demands the ultimate commitment to the job… to die, for a living.', 137, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days', 'PG_13'),
    ('Oppenheimer', 'The story of J. Robert Oppenheimer''s role in the development of the atomic bomb during World War II.', 181, '2023-08-20', CURRENT_DATE + INTERVAL '30 days', 'PG_13'),
    ('Interstellar', 'The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.', 169, '2014-11-06', CURRENT_DATE + INTERVAL '30 days', 'PG_13'),
    ('Once Upon a Time... in Hollywood', 'Los Angeles, 1969. TV star Rick Dalton, a struggling actor specializing in westerns, and stuntman Cliff Booth, his best friend, try to survive in a constantly changing movie industry. Dalton is the neighbor of the young and promising actress and model Sharon Tate, who has just married the prestigious Polish director Roman Polanski…', 162, '2019-07-26', CURRENT_DATE + INTERVAL '30 days', 'PG_13'),
    ('Pirates of the Caribbean: The Curse of the Black Pearl', 'After Port Royal is attacked and pillaged by a mysterious pirate crew, capturing the governor''s daughter Elizabeth Swann in the process, William Turner asks free-willing pirate Jack Sparrow to help him locate the crew''s ship—The Black Pearl—so that he can rescue the woman he loves.', 162, '2003-07-09', CURRENT_DATE + INTERVAL '30 days', 'PG_13'),

    -- start date in the future to simulate upcoming movies--
    ('Minecraft', 'Four misfits find themselves struggling with ordinary problems when they are suddenly pulled through a mysterious portal into the Overworld: a bizarre, cubic wonderland that thrives on imagination. To get back home, they''ll have to master this world while embarking on a magical quest with an unexpected, expert crafter, Steve.', 120, CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 'PG_13'),
    ('Captain America: Brave New World', 'After meeting with newly elected U.S. President Thaddeus Ross, Sam finds himself in the middle of an international incident. He must discover the reason behind a nefarious global plot before the true mastermind has the entire world seeing red.', 120, CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 'PG_13'),
    ('The Union', 'A New Jersey construction worker goes from regular guy to aspiring spy when his long-lost high school sweetheart recruits him for an espionage mission.', 109, CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 'PG_13'),
    ('Gladiator II', 'Years after witnessing the death of the revered hero Maximus at the hands of his uncle, Lucius is forced to enter the Colosseum after his home is conquered by the tyrannical Emperors who now lead Rome with an iron fist. With rage in his heart and the future of the Empire at stake, Lucius must look to his past to find strength and honor to return the glory of Rome to its people.', 148, CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 'PG_13'),
    ('Blink Twice', 'When tech billionaire Slater King meets cocktail waitress Frida at his fundraising gala, he invites her to join him and his friends on a dream vacation on his private island. But despite the epic setting, beautiful people, ever-flowing champagne, and late-night dance parties, Frida can sense that there’s something sinister hiding beneath the island’s lush façade.', 102, CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 'PG_13'),
    ('Wicked', 'In the land of Oz, ostracized and misunderstood green-skinned Elphaba is forced to share a room with the popular aristocrat Glinda at Shiz University, and the two''s unlikely friendship is tested as they begin to fulfill their respective destinies as Glinda the Good and the Wicked Witch of the West.', 162, CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 'PG');

WITH movie_genres AS (
    SELECT
        m.id AS movie_id,
        g.id AS genre_id
    FROM
        movies m
            CROSS JOIN genres g
    WHERE
        (m.title = 'Avatar: The Way of Water' AND g.name IN ('Sci-Fi', 'Adventure', 'Action'))
       OR
        (m.title = 'Mickey 17' AND g.name IN ('Sci-Fi', 'Thriller'))
       OR
        (m.title = 'Oppenheimer' AND g.name IN ('Drama', 'History'))
       OR
        (m.title = 'Interstellar' AND g.name IN ('Adventure', 'Drama', 'Sci-Fi'))
       OR
        (m.title = 'Once Upon a Time... in Hollywood' AND g.name IN ('Comedy', 'Drama', 'Thriller'))
       OR
        (m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND g.name IN ('Adventure', 'Fantasy', 'Action'))
       OR
        (m.title = 'Minecraft' AND g.name IN ('Family', 'Comedy', 'Adventure', 'Fantasy'))
       OR
        (m.title = 'Captain America: Brave New World' AND g.name IN ('Action', 'Thriller', 'Sci-Fi'))
       OR
        (m.title = 'The Union' AND g.name IN ('Action', 'Comedy'))
       OR
        (m.title = 'Gladiator II' AND g.name IN ('Action', 'Adventure', 'Drama'))
       OR
        (m.title = 'Blink Twice' AND g.name IN ('Mystery', 'Thriller'))
       OR
        (m.title = 'Wicked' AND g.name IN ('Drama', 'Romance', 'Fantasy'))
)
INSERT INTO movie_genre (movie_id, genre_id)
SELECT movie_id, genre_id FROM movie_genres;

INSERT INTO movie_photos (url, is_cover_image, movie_id)
VALUES
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Avatar/avatar-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Avatar/avatar2.jpg', false,
     (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Mickey17/mickey-17-k5.jpg', true,
     (SELECT id FROM movies WHERE title = 'Mickey 17')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Oppenheimer/oppeneimer-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Oppenheimer')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Interstellar/interstellar-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Interstellar')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlackPearl/blackpearl-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/OnceUpon/onceupon-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Minecraft/minecraft-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Minecraft')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/CaptainAmericaBraveNewWorld/captainamerica-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Captain America: Brave New World')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/TheUnion/theunion-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'The Union')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/GladiatorII/gladiator2-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Gladiator II')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlinkTwice/blinktwice-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Blink Twice')),
    ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Wicked/wicked-cover.jpg', true,
     (SELECT id FROM movies WHERE title = 'Wicked'));

COMMIT;
