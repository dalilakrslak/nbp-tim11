CREATE TABLE actors (
                        id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                        first_name VARCHAR2(255) NOT NULL,
                        last_name VARCHAR2(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
                       id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                       movie_id RAW(16) NOT NULL,
                       actor_id RAW(16) NOT NULL,
                       name VARCHAR2(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_roles_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
                       CONSTRAINT fk_roles_actor FOREIGN KEY (actor_id) REFERENCES actors(id) ON DELETE CASCADE
);

CREATE TABLE writers (
                         id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                         first_name VARCHAR2(255) NOT NULL,
                         last_name VARCHAR2(255) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movie_writer (
                              id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                              movie_id RAW(16) NOT NULL,
                              writer_id RAW(16) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_movie_writer_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
                              CONSTRAINT fk_movie_writer_writer FOREIGN KEY (writer_id) REFERENCES writers(id) ON DELETE CASCADE,
                              CONSTRAINT uq_movie_writer UNIQUE (movie_id, writer_id)
);

CREATE INDEX idx_role_movie_id ON roles (movie_id);
CREATE INDEX idx_role_actor_id ON roles (actor_id);
CREATE INDEX idx_movie_writer_movie_id ON movie_writer (movie_id);
CREATE INDEX idx_movie_writer_writer_id ON movie_writer (writer_id);

ALTER TABLE movies ADD trailer_url VARCHAR2(1000);
ALTER TABLE movies ADD director VARCHAR2(255);

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=d9MyW72ELq0&t=7s&ab_channel=Avatar',
                  director = 'James Cameron'
WHERE title = 'Avatar: The Way of Water';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=osYpGSz_0i4&ab_channel=WarnerBros.',
                  director = 'Bong Joon-ho'
WHERE title = 'Mickey 17';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=uYPbbksJxIg&ab_channel=UniversalPictures',
                  director = 'Christopher Nolan'
WHERE title = 'Oppenheimer';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=zSWdZVtXT7E&ab_channel=WarnerBros.UK%26Ireland',
                  director = 'Christopher Nolan'
WHERE title = 'Interstellar';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=ELeMaP8EPAA&ab_channel=SonyPicturesEntertainment',
                  director = 'Quentin Tarantino'
WHERE title = 'Once Upon a Time... in Hollywood';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=naQr0uTrH_s&ab_channel=RottenTomatoesClassicTrailers',
                  director = 'Gore Verbinski'
WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=wJO_vIDZn-I&ab_channel=WarnerBros.',
                  director = 'Jared Hess'
WHERE title = 'Minecraft';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=1pHDWnXmK7Y&ab_channel=MarvelEntertainment',
                  director = 'Julius Onah'
WHERE title = 'Captain America: Brave New World';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=vea9SdnRMyg&ab_channel=Netflix',
                  director = 'Julian Farino'
WHERE title = 'The Union';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=4rgYUipGJNo&ab_channel=ParamountPictures',
                  director = 'Ridley Scott'
WHERE title = 'Gladiator II';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=aMcmfonGWY4&ab_channel=AmazonMGMStudios',
                  director = 'Zoë Kravitz'
WHERE title = 'Blink Twice';

UPDATE movies SET
                  trailer_url = 'https://www.youtube.com/watch?v=6COmYeLsz4c&ab_channel=UniversalPictures',
                  director = 'Jon M. Chu'
WHERE title = 'Wicked';

ALTER TABLE movies MODIFY trailer_url VARCHAR2(1000) NOT NULL;
ALTER TABLE movies MODIFY director VARCHAR2(255) NOT NULL;

INSERT ALL
    INTO actors (first_name, last_name) VALUES ('Sam', 'Worthington')
    INTO actors (first_name, last_name) VALUES ('Zoe', 'Saldana')
    INTO actors (first_name, last_name) VALUES ('Robert', 'Pattinson')
    INTO actors (first_name, last_name) VALUES ('Steven', 'Yeun')
    INTO actors (first_name, last_name) VALUES ('Cillian', 'Murphy')
    INTO actors (first_name, last_name) VALUES ('Emily', 'Blunt')
    INTO actors (first_name, last_name) VALUES ('Matthew', 'McConaughey')
    INTO actors (first_name, last_name) VALUES ('Anne', 'Hathaway')
    INTO actors (first_name, last_name) VALUES ('Leonardo', 'DiCaprio')
    INTO actors (first_name, last_name) VALUES ('Brad', 'Pitt')
    INTO actors (first_name, last_name) VALUES ('Johnny', 'Depp')
    INTO actors (first_name, last_name) VALUES ('Keira', 'Knightley')
    INTO actors (first_name, last_name) VALUES ('Jason', 'Momoa')
    INTO actors (first_name, last_name) VALUES ('Jack', 'Black')
    INTO actors (first_name, last_name) VALUES ('Anthony', 'Mackie')
    INTO actors (first_name, last_name) VALUES ('Harrison', 'Ford')
    INTO actors (first_name, last_name) VALUES ('Mark', 'Wahlberg')
    INTO actors (first_name, last_name) VALUES ('Halle', 'Berry')
    INTO actors (first_name, last_name) VALUES ('Paul', 'Mescal')
    INTO actors (first_name, last_name) VALUES ('Denzel', 'Washington')
    INTO actors (first_name, last_name) VALUES ('Channing', 'Tatum')
    INTO actors (first_name, last_name) VALUES ('Naomi', 'Ackie')
    INTO actors (first_name, last_name) VALUES ('Cynthia', 'Erivo')
    INTO actors (first_name, last_name) VALUES ('Ariana', 'Grande')
    INTO actors (first_name, last_name) VALUES ('Kate', 'Winslet')
    INTO actors (first_name, last_name) VALUES ('Giovanni', 'Ribisi')
    INTO actors (first_name, last_name) VALUES ('Stephen', 'Lang')
    INTO actors (first_name, last_name) VALUES ('Cliff', 'Curtis')
    INTO actors (first_name, last_name) VALUES ('Naomi', 'Scott')
    INTO actors (first_name, last_name) VALUES ('Daniel', 'Kaluuya')
    INTO actors (first_name, last_name) VALUES ('Pom', 'Klementieff')
    INTO actors (first_name, last_name) VALUES ('Benedict', 'Wong')
    INTO actors (first_name, last_name) VALUES ('Matt', 'Damon')
    INTO actors (first_name, last_name) VALUES ('Gary', 'Oldman')
    INTO actors (first_name, last_name) VALUES ('Florence', 'Pugh')
    INTO actors (first_name, last_name) VALUES ('Josh', 'Hartnett')
    INTO actors (first_name, last_name) VALUES ('Jessica', 'Chastain')
    INTO actors (first_name, last_name) VALUES ('Michael', 'Caine')
    INTO actors (first_name, last_name) VALUES ('Casey', 'Affleck')
    INTO actors (first_name, last_name) VALUES ('Topher', 'Grace')
    INTO actors (first_name, last_name) VALUES ('Al', 'Pacino')
    INTO actors (first_name, last_name) VALUES ('Dakota', 'Fanning')
    INTO actors (first_name, last_name) VALUES ('Timothy', 'Olyphant')
    INTO actors (first_name, last_name) VALUES ('Luke', 'Perry')
    INTO actors (first_name, last_name) VALUES ('Geoffrey', 'Rush')
    INTO actors (first_name, last_name) VALUES ('Orlando', 'Bloom')
    INTO actors (first_name, last_name) VALUES ('Jonathan', 'Pryce')
    INTO actors (first_name, last_name) VALUES ('Lee', 'Arenberg')
    INTO actors (first_name, last_name) VALUES ('Emma', 'Stone')
    INTO actors (first_name, last_name) VALUES ('Finn', 'Wolfhard')
    INTO actors (first_name, last_name) VALUES ('David', 'Harbour')
    INTO actors (first_name, last_name) VALUES ('Millie', 'Bobby Brown')
    INTO actors (first_name, last_name) VALUES ('Liv', 'Tyler')
    INTO actors (first_name, last_name) VALUES ('Tim', 'Blake Nelson')
    INTO actors (first_name, last_name) VALUES ('Sebastian', 'Stan')
    INTO actors (first_name, last_name) VALUES ('Shira', 'Haas')
    INTO actors (first_name, last_name) VALUES ('Jessica', 'Alba')
    INTO actors (first_name, last_name) VALUES ('Tom', 'Holland')
    INTO actors (first_name, last_name) VALUES ('Vin', 'Diesel')
    INTO actors (first_name, last_name) VALUES ('Eva', 'Mendes')
    INTO actors (first_name, last_name) VALUES ('Pedro', 'Pascal')
    INTO actors (first_name, last_name) VALUES ('Joseph', 'Quinn')
    INTO actors (first_name, last_name) VALUES ('Barry', 'Keoghan')
    INTO actors (first_name, last_name) VALUES ('Connie', 'Nielsen')
    INTO actors (first_name, last_name) VALUES ('Sydney', 'Sweeney')
    INTO actors (first_name, last_name) VALUES ('Chris', 'Pine')
    INTO actors (first_name, last_name) VALUES ('Ayo', 'Edebiri')
    INTO actors (first_name, last_name) VALUES ('Dakota', 'Johnson')
    INTO actors (first_name, last_name) VALUES ('Jonathan', 'Bailey')
    INTO actors (first_name, last_name) VALUES ('Michelle', 'Yeoh')
    INTO actors (first_name, last_name) VALUES ('Jeff', 'Goldblum')
    INTO actors (first_name, last_name) VALUES ('Bowen', 'Yang')
SELECT 1 FROM dual;

INSERT ALL
    INTO writers (first_name, last_name) VALUES ('James', 'Cameron')
    INTO writers (first_name, last_name) VALUES ('Rick', 'Jaffa')
    INTO writers (first_name, last_name) VALUES ('Amanda', 'Silver')
    INTO writers (first_name, last_name) VALUES ('Bong', 'Joon-ho')
    INTO writers (first_name, last_name) VALUES ('Christopher', 'Nolan')
    INTO writers (first_name, last_name) VALUES ('Emma', 'Thomas')
    INTO writers (first_name, last_name) VALUES ('Quentin', 'Tarantino')
    INTO writers (first_name, last_name) VALUES ('Ted', 'Elliott')
    INTO writers (first_name, last_name) VALUES ('Terry', 'Rossio')
    INTO writers (first_name, last_name) VALUES ('Allison', 'Schroeder')
    INTO writers (first_name, last_name) VALUES ('Malcolm', 'Spellman')
    INTO writers (first_name, last_name) VALUES ('Dalan', 'Musson')
    INTO writers (first_name, last_name) VALUES ('Drew', 'Pearce')
    INTO writers (first_name, last_name) VALUES ('Joe', 'Shrapnel')
    INTO writers (first_name, last_name) VALUES ('David', 'Scarpa')
    INTO writers (first_name, last_name) VALUES ('E.T.', 'Feigenbaum')
    INTO writers (first_name, last_name) VALUES ('Winnie', 'Holzman')
    INTO writers (first_name, last_name) VALUES ('Gregory', 'Maguire')
SELECT 1 FROM dual;

INSERT INTO movie_writer (movie_id, writer_id)
WITH movie_ids AS (
    SELECT id, title FROM movies
),
     writer_ids AS (
         SELECT id, first_name, last_name FROM writers
     )
SELECT
    m.id,
    w.id
FROM movie_ids m, writer_ids w
WHERE
    (m.title = 'Avatar: The Way of Water' AND (
        (w.first_name = 'James' AND w.last_name = 'Cameron') OR
        (w.first_name = 'Rick' AND w.last_name = 'Jaffa') OR
        (w.first_name = 'Amanda' AND w.last_name = 'Silver')
        )) OR
    (m.title = 'Mickey 17' AND w.first_name = 'Bong' AND w.last_name = 'Joon-ho') OR
    (m.title = 'Oppenheimer' AND w.first_name = 'Christopher' AND w.last_name = 'Nolan') OR
    (m.title = 'Interstellar' AND w.first_name = 'Christopher' AND w.last_name = 'Nolan') OR
    (m.title = 'Interstellar' AND w.first_name = 'Emma' AND w.last_name = 'Thomas') OR
    (m.title = 'Once Upon a Time... in Hollywood' AND w.first_name = 'Quentin' AND w.last_name = 'Tarantino') OR
    (m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND w.first_name = 'Ted' AND w.last_name = 'Elliott') OR
    (m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND w.first_name = 'Terry' AND w.last_name = 'Rossio') OR
    (m.title = 'Minecraft' AND w.first_name = 'Allison' AND w.last_name = 'Schroeder') OR
    (m.title = 'Captain America: Brave New World' AND w.first_name = 'Malcolm' AND w.last_name = 'Spellman') OR
    (m.title = 'Captain America: Brave New World' AND w.first_name = 'Dalan' AND w.last_name = 'Musson') OR
    (m.title = 'The Union' AND w.first_name = 'Drew' AND w.last_name = 'Pearce') OR
    (m.title = 'The Union' AND w.first_name = 'Joe' AND w.last_name = 'Shrapnel') OR
    (m.title = 'Gladiator II' AND w.first_name = 'David' AND w.last_name = 'Scarpa') OR
    (m.title = 'Blink Twice' AND w.first_name = 'E.T.' AND w.last_name = 'Feigenbaum') OR
    (m.title = 'Wicked' AND w.first_name = 'Winnie' AND w.last_name = 'Holzman') OR
    (m.title = 'Wicked' AND w.first_name = 'Gregory' AND w.last_name = 'Maguire');

INSERT INTO roles (movie_id, actor_id, name)
WITH movie_ids AS (
    SELECT id, title FROM movies
),
     actor_ids AS (
         SELECT id, first_name, last_name FROM actors
     )
SELECT
    m.id,
    a.id,
    CASE
        WHEN m.title = 'Avatar: The Way of Water' AND a.first_name = 'Sam' AND a.last_name = 'Worthington' THEN 'Jake Sully'
        WHEN m.title = 'Avatar: The Way of Water' AND a.first_name = 'Zoe' AND a.last_name = 'Saldana' THEN 'Neytiri'
        WHEN m.title = 'Mickey 17' AND a.first_name = 'Robert' AND a.last_name = 'Pattinson' THEN 'Mickey Barnes'
        WHEN m.title = 'Mickey 17' AND a.first_name = 'Steven' AND a.last_name = 'Yeun' THEN 'Bong Character'
        WHEN m.title = 'Oppenheimer' AND a.first_name = 'Cillian' AND a.last_name = 'Murphy' THEN 'J. Robert Oppenheimer'
        WHEN m.title = 'Oppenheimer' AND a.first_name = 'Emily' AND a.last_name = 'Blunt' THEN 'Katherine Oppenheimer'
        WHEN m.title = 'Interstellar' AND a.first_name = 'Matthew' AND a.last_name = 'McConaughey' THEN 'Joseph Cooper'
        WHEN m.title = 'Interstellar' AND a.first_name = 'Anne' AND a.last_name = 'Hathaway' THEN 'Dr. Amelia Brand'
        WHEN m.title = 'Once Upon a Time... in Hollywood' AND a.first_name = 'Leonardo' AND a.last_name = 'DiCaprio' THEN 'Rick Dalton'
        WHEN m.title = 'Once Upon a Time... in Hollywood' AND a.first_name = 'Brad' AND a.last_name = 'Pitt' THEN 'Cliff Booth'
        WHEN m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND a.first_name = 'Johnny' AND a.last_name = 'Depp' THEN 'Captain Jack Sparrow'
        WHEN m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND a.first_name = 'Keira' AND a.last_name = 'Knightley' THEN 'Elizabeth Swann'
        WHEN m.title = 'Minecraft' AND a.first_name = 'Jason' AND a.last_name = 'Momoa' THEN 'Steve'
        WHEN m.title = 'Minecraft' AND a.first_name = 'Jack' AND a.last_name = 'Black' THEN 'Notch'
        WHEN m.title = 'Captain America: Brave New World' AND a.first_name = 'Anthony' AND a.last_name = 'Mackie' THEN 'Sam Wilson / Captain America'
        WHEN m.title = 'Captain America: Brave New World' AND a.first_name = 'Harrison' AND a.last_name = 'Ford' THEN 'Thaddeus Ross'
        WHEN m.title = 'The Union' AND a.first_name = 'Mark' AND a.last_name = 'Wahlberg' THEN 'Mike'
        WHEN m.title = 'The Union' AND a.first_name = 'Halle' AND a.last_name = 'Berry' THEN 'Roxanne'
        WHEN m.title = 'Gladiator II' AND a.first_name = 'Paul' AND a.last_name = 'Mescal' THEN 'Lucius'
        WHEN m.title = 'Gladiator II' AND a.first_name = 'Denzel' AND a.last_name = 'Washington' THEN 'Macrinus'
        WHEN m.title = 'Blink Twice' AND a.first_name = 'Channing' AND a.last_name = 'Tatum' THEN 'Slater King'
        WHEN m.title = 'Blink Twice' AND a.first_name = 'Naomi' AND a.last_name = 'Ackie' THEN 'Frida'
        WHEN m.title = 'Wicked' AND a.first_name = 'Cynthia' AND a.last_name = 'Erivo' THEN 'Elphaba'
        WHEN m.title = 'Wicked' AND a.first_name = 'Ariana' AND a.last_name = 'Grande' THEN 'Glinda'
        WHEN m.title = 'Avatar: The Way of Water' AND a.first_name = 'Kate' AND a.last_name = 'Winslet' THEN 'Ronal'
        WHEN m.title = 'Avatar: The Way of Water' AND a.first_name = 'Giovanni' AND a.last_name = 'Ribisi' THEN 'Parker Selfridge'
        WHEN m.title = 'Avatar: The Way of Water' AND a.first_name = 'Stephen' AND a.last_name = 'Lang' THEN 'Colonel Quaritch'
        WHEN m.title = 'Avatar: The Way of Water' AND a.first_name = 'Cliff' AND a.last_name = 'Curtis' THEN 'Tonowari'
        WHEN m.title = 'Mickey 17' AND a.first_name = 'Naomi' AND a.last_name = 'Ackie' THEN 'Elara'
        WHEN m.title = 'Mickey 17' AND a.first_name = 'Daniel' AND a.last_name = 'Henshall' THEN 'Commander Voss'
        WHEN m.title = 'Mickey 17' AND a.first_name = 'Pom' AND a.last_name = 'Klementieff' THEN 'Unity Drone'
        WHEN m.title = 'Mickey 17' AND a.first_name = 'Benedict' AND a.last_name = 'Wong' THEN 'Scientist Xi'
        WHEN m.title = 'Oppenheimer' AND a.first_name = 'Matt' AND a.last_name = 'Damon' THEN 'Leslie Groves'
        WHEN m.title = 'Oppenheimer' AND a.first_name = 'Gary' AND a.last_name = 'Oldman' THEN 'Lewis Strauss'
        WHEN m.title = 'Oppenheimer' AND a.first_name = 'Florence' AND a.last_name = 'Pugh' THEN 'Jean Tatlock'
        WHEN m.title = 'Oppenheimer' AND a.first_name = 'Josh' AND a.last_name = 'Hartnett' THEN 'Ernest Lawrence'
        WHEN m.title = 'Interstellar' AND a.first_name = 'Jessica' AND a.last_name = 'Chastain' THEN 'Murph'
        WHEN m.title = 'Interstellar' AND a.first_name = 'Michael' AND a.last_name = 'Caine' THEN 'Professor Brand'
        WHEN m.title = 'Interstellar' AND a.first_name = 'Casey' AND a.last_name = 'Affleck' THEN 'Tom Cooper'
        WHEN m.title = 'Interstellar' AND a.first_name = 'Topher' AND a.last_name = 'Grace' THEN 'Getty'
        WHEN m.title = 'Once Upon a Time... in Hollywood' AND a.first_name = 'Al' AND a.last_name = 'Pacino' THEN 'Marvin Schwarz'
        WHEN m.title = 'Once Upon a Time... in Hollywood' AND a.first_name = 'Dakota' AND a.last_name = 'Fanning' THEN 'Squeaky Fromme'
        WHEN m.title = 'Once Upon a Time... in Hollywood' AND a.first_name = 'Timothy' AND a.last_name = 'Olyphant' THEN 'Jim Stacy'
        WHEN m.title = 'Once Upon a Time... in Hollywood' AND a.first_name = 'Luke' AND a.last_name = 'Perry' THEN 'Wayne Maunder'
        WHEN m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND a.first_name = 'Geoffrey' AND a.last_name = 'Rush' THEN 'Barbossa'
        WHEN m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND a.first_name = 'Orlando' AND a.last_name = 'Bloom' THEN 'Will Turner'
        WHEN m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND a.first_name = 'Jonathan' AND a.last_name = 'Pryce' THEN 'Governor Swann'
        WHEN m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND a.first_name = 'Lee' AND a.last_name = 'Arenberg' THEN 'Pintel'
        WHEN m.title = 'Minecraft' AND a.first_name = 'Emma' AND a.last_name = 'Myers' THEN 'Redstone Engineer'
        WHEN m.title = 'Minecraft' AND a.first_name = 'Finn' AND a.last_name = 'Wolfhard' THEN 'Village Kid'
        WHEN m.title = 'Minecraft' AND a.first_name = 'David' AND a.last_name = 'Thewlis' THEN 'Iron Golem'
        WHEN m.title = 'Minecraft' AND a.first_name = 'Millie' AND a.last_name = 'Bobby Brown' THEN 'Explorer'
        WHEN m.title = 'Captain America: Brave New World' AND a.first_name = 'Liv' AND a.last_name = 'Tyler' THEN 'Betty Ross'
        WHEN m.title = 'Captain America: Brave New World' AND a.first_name = 'Tim' AND a.last_name = 'Blake Nelson' THEN 'Samuel Sterns'
        WHEN m.title = 'Captain America: Brave New World' AND a.first_name = 'Sebastian' AND a.last_name = 'Stan' THEN 'Bucky Barnes'
        WHEN m.title = 'Captain America: Brave New World' AND a.first_name = 'Shira' AND a.last_name = 'Haas' THEN 'Sabra'
        WHEN m.title = 'The Union' AND a.first_name = 'Jessica' AND a.last_name = 'De Gouw' THEN 'Agent X'
        WHEN m.title = 'The Union' AND a.first_name = 'Tom' AND a.last_name = 'Hopper' THEN 'Rookie Cop'
        WHEN m.title = 'The Union' AND a.first_name = 'Vin' AND a.last_name = 'Diesel' THEN 'The Driver'
        WHEN m.title = 'The Union' AND a.first_name = 'Eva' AND a.last_name = 'Green' THEN 'Rival Spy'
        WHEN m.title = 'Gladiator II' AND a.first_name = 'Pedro' AND a.last_name = 'Pascal' THEN 'General Decimus'
        WHEN m.title = 'Gladiator II' AND a.first_name = 'Joseph' AND a.last_name = 'Quinn' THEN 'Young Soldier'
        WHEN m.title = 'Gladiator II' AND a.first_name = 'Barry' AND a.last_name = 'Keoghan' THEN 'Emperor Geta'
        WHEN m.title = 'Gladiator II' AND a.first_name = 'Connie' AND a.last_name = 'Nielsen' THEN 'Lucilla'
        WHEN m.title = 'Blink Twice' AND a.first_name = 'Sydney' AND a.last_name = 'Sweeney' THEN 'Isla'
        WHEN m.title = 'Blink Twice' AND a.first_name = 'Chris' AND a.last_name = 'Messina' THEN 'The Influencer'
        WHEN m.title = 'Blink Twice' AND a.first_name = 'Ayo' AND a.last_name = 'Edebiri' THEN 'Jade'
        WHEN m.title = 'Blink Twice' AND a.first_name = 'Dakota' AND a.last_name = 'Johnson' THEN 'Lana'
        WHEN m.title = 'Wicked' AND a.first_name = 'Jonathan' AND a.last_name = 'Bailey' THEN 'Fiyero'
        WHEN m.title = 'Wicked' AND a.first_name = 'Michelle' AND a.last_name = 'Yeoh' THEN 'Madame Morrible'
        WHEN m.title = 'Wicked' AND a.first_name = 'Jeff' AND a.last_name = 'Goldblum' THEN 'The Wizard'
        WHEN m.title = 'Wicked' AND a.first_name = 'Bowen' AND a.last_name = 'Yang' THEN 'Boq'
        END
FROM movie_ids m, actor_ids a
WHERE (
          (m.title = 'Avatar: The Way of Water' AND a.last_name IN ('Worthington','Saldana','Winslet','Ribisi','Lang','Curtis'))
              OR (m.title = 'Mickey 17' AND a.last_name IN ('Pattinson','Yeun','Ackie','Henshall','Klementieff','Wong'))
              OR (m.title = 'Oppenheimer' AND a.last_name IN ('Murphy','Blunt','Damon','Oldman','Pugh','Hartnett'))
              OR (m.title = 'Interstellar' AND a.last_name IN ('McConaughey','Hathaway','Chastain','Caine','Affleck','Grace'))
              OR (m.title = 'Once Upon a Time... in Hollywood' AND a.last_name IN ('DiCaprio','Pitt','Pacino','Fanning','Olyphant','Perry'))
              OR (m.title = 'Pirates of the Caribbean: The Curse of the Black Pearl' AND a.last_name IN ('Depp','Knightley','Rush','Bloom','Pryce','Arenberg'))
              OR (m.title = 'Minecraft' AND a.last_name IN ('Momoa','Black','Myers','Wolfhard','Thewlis','Bobby Brown'))
              OR (m.title = 'Captain America: Brave New World' AND a.last_name IN ('Mackie','Ford','Tyler','Blake Nelson','Stan','Haas'))
              OR (m.title = 'The Union' AND a.last_name IN ('Wahlberg','Berry','De Gouw','Hopper','Diesel','Green'))
              OR (m.title = 'Gladiator II' AND a.last_name IN ('Mescal','Washington','Pascal','Quinn','Keoghan','Nielsen'))
              OR (m.title = 'Blink Twice' AND a.last_name IN ('Tatum','Ackie','Sweeney','Messina','Edebiri','Johnson'))
              OR (m.title = 'Wicked' AND a.last_name IN ('Erivo','Grande','Bailey','Yeoh','Goldblum','Yang'))
          );

INSERT ALL
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Avatar/4pNlHx6ytdYBDs94PgcS0wQkbc4.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Avatar/rZAUAePCueGzTdDzRQe9wD8x1Ov.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Avatar/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Mickey17/2S3hRv6KqoZ3vReqlTVk1aHJIU6.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Mickey17/7Oh1xRB8QbMduhqXEUHKlnwxMJi.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Mickey17/cyDndCTSLg6GGVvDQLsaV4fXJzC.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Mickey17/qUc0Hol3eP74dbW4YyqT6oRLYgT.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Oppenheimer/8szKvTWhqnatqrHWloFyyPX1WZc.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Oppenheimer/h0TuquPlfxqe4sJSy7sUlEzaAsL.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Oppenheimer/kMa1TSDj76zTSleXE7xsuZ4s3i0.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Oppenheimer/ycnO0cjsAROSGJKuMODgRtWsHQw.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Interstellar/5C3RriLKkIAQtQMx85JLtu4rVI2.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Interstellar/l33oR0mnvf20avWyIMxW02EtQxn.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Interstellar/l38yk8r3RLzLYgFFvRYcOiDbvcq.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Interstellar/ln2Gre4IYRhpjuGVybbtaF4CLo5.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlackPearl/16FT0cxxBK9qQSvZf7F8i0ITQPm.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlackPearl/dm0Q0RtV6U8pgM0mXvsNJpwRayP.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlackPearl/nr6SuKESwEPZnExvxSzQczF1iBt.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlackPearl/tDNHCZpTaLhVF7awv1PYgDkvHJU.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/OnceUpon/ltUKAxoQ4GRu7EaUNg8GxD9vZ6u.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/OnceUpon/nmsutFaFnlY1N85mPDt0r4fWynL.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/OnceUpon/oRiUKwDpcqDdoLwPoA4FIRh3hqY.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/OnceUpon/vm8C7lAob4hSn8MvHGa9RBLy7rR.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Minecraft/4MWc9Ur80Wo0B1fVVTnV0CoSh6A.jpg', 0, (SELECT id FROM movies WHERE title = 'Minecraft'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Minecraft/56lv8d5yst1SBhw1uR3iGFd1R43.jpg', 0, (SELECT id FROM movies WHERE title = 'Minecraft'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Minecraft/lEkq2xPYtvEGx2iKkClN3p5uxFQ.jpg', 0, (SELECT id FROM movies WHERE title = 'Minecraft'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Minecraft/ws2UKX5dtQlK6sJWbVaikCOJbxD.jpg', 0, (SELECT id FROM movies WHERE title = 'Minecraft'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/CaptainAmericaBraveNewWorld/4ybBpFNwTQCbBewWrpO2YFNWRuB.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/CaptainAmericaBraveNewWorld/eJLTpgUAFkx165LuUoQqQGyN5Wp.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/CaptainAmericaBraveNewWorld/ncTtBRGnohOCMKfVuu3AfzaL1xE.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/CaptainAmericaBraveNewWorld/qfAfE5auxsuxhxPpnETRAyTP5ff.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/TheUnion/ADJon1zAvcParV7A1e6q6z0KVQ.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/TheUnion/dqsWbl7aubfUn6OqmkmXPISj2W6.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/TheUnion/mFhihsHSoyIqZbK7IgspWwq23ww.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/TheUnion/vRHOXQhTrlWp0Hzr1b5Qn2Fa3bx.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/GladiatorII/8mjYwWT50GkRrrRdyHzJorfEfcl.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/GladiatorII/A4SDLzUM9RJVdQc3gWOX4epGLM7.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/GladiatorII/bHeUgZKqduubnNl8GshjrpHS9lF.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/GladiatorII/xGv28mcf8vdFttD0KgI5GnDDlkG.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlinkTwice/11xVvX1H7alRDfGHztA4lNvocDh.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlinkTwice/h6K9vwhbogdqXMxIpFpqKS9bEjQ.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlinkTwice/yUJpsoVT51seW9YG3jrRh9JS5S0.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/BlinkTwice/zQuOiqlktwgR64WW07PFWpjxWGs.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Wicked/830sdPfXpyizgm05HR3LR6vFJrL.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Wicked/jTOeWjamUKGxWVUO1TMZXqQUarw.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Wicked/k4xavRmJUzf3M2LRDxhlPq8R6zy.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://wzdbqcgtygvmtfdajvio.supabase.co/storage/v1/object/public/CinemaAppPhotos/vsfcbekllchxbgylfrta/images/movies/Wicked/uDjYG4ODYetiNuRaopvLvRq0RuO.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
SELECT 1 FROM dual;

UPDATE movies
SET title = 'A Minecraft Movie'
WHERE title = 'Minecraft';

ALTER TABLE movie_photos MODIFY is_cover_image DEFAULT 0;