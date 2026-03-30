-- Add language column to movies table
ALTER TABLE movies ADD language VARCHAR(50) DEFAULT 'English';

-- Create screenings table
CREATE TABLE screenings (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    movie_id RAW(16) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_movie_screenings FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- Add screenings for each movie
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(18, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(13, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(20, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));

INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(14, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(19, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(10, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(14, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(18, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(11, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Oppenheimer'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(16, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(19, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(14, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Interstellar'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(18, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(19, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(10, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(17, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'), CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(12, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(18, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(13, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Minecraft'), (SELECT start_date FROM movies WHERE title = 'Minecraft') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(20, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(11, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(14, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT start_date FROM movies WHERE title = 'Captain America: Brave New World') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(10, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'The Union'), (SELECT start_date FROM movies WHERE title = 'The Union') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(12, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(15, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(18, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Gladiator II'), (SELECT start_date FROM movies WHERE title = 'Gladiator II') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(17, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(14, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(18, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(12, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Blink Twice'), (SELECT start_date FROM movies WHERE title = 'Blink Twice') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(16, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));


INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(10, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(13, 'HOUR') + NUMTODSINTERVAL(15, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(17, 'HOUR'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(11, 'HOUR') + NUMTODSINTERVAL(45, 'MINUTE'));
INSERT INTO screenings (movie_id, start_time)
VALUES
    ((SELECT id FROM movies WHERE title = 'Wicked'), (SELECT start_date FROM movies WHERE title = 'Wicked') + NUMTODSINTERVAL(1, 'DAY') + NUMTODSINTERVAL(15, 'HOUR') + NUMTODSINTERVAL(30, 'MINUTE'));
