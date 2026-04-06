INSERT ALL
    INTO locations (city, country) VALUES ('Tuzla', 'Bosnia and Herzegovina')
    INTO locations (city, country) VALUES ('Mostar', 'Bosnia and Herzegovina')
    INTO locations (city, country) VALUES ('Banja Luka', 'Bosnia and Herzegovina')
    INTO locations (city, country) VALUES ('Zenica', 'Bosnia and Herzegovina')
    INTO locations (city, country) VALUES ('Brcko', 'Bosnia and Herzegovina')
    INTO locations (city, country) VALUES ('Zagreb', 'Croatia')
    INTO locations (city, country) VALUES ('Belgrade', 'Serbia')
    INTO locations (city, country) VALUES ('Ljubljana', 'Slovenia')
    INTO locations (city, country) VALUES ('Split', 'Croatia')
SELECT 1 FROM dual;

COMMIT;


INSERT ALL
    INTO venues (name, street, image_url, location_id) 
        VALUES ('CineStar Tuzla', 'Trg Slobode 1, Tuzla',
        'https://example.com/tuzla-cinestar.jpg',
        (SELECT id FROM locations WHERE city = 'Tuzla'))

    INTO venues (name, street, image_url, location_id) 
        VALUES ('Kino Mostar', 'Kneza Domagoja bb, Mostar',
        'https://example.com/mostar-kino.jpg',
        (SELECT id FROM locations WHERE city = 'Mostar'))

    INTO venues (name, street, image_url, location_id) 
        VALUES ('Palas Cinema', 'Kralja Petra I Karađorđevića 103, Banja Luka',
        'https://example.com/palas.jpg',
        (SELECT id FROM locations WHERE city = 'Banja Luka'))

    INTO venues (name, street, image_url, location_id) 
        VALUES ('Multiplex Ekran Zenica', 'Kamberovića Polje bb, Zenica',
        'https://example.com/zenica-ekran.jpg',
        (SELECT id FROM locations WHERE city = 'Zenica'))

    INTO venues (name, street, image_url, location_id) 
        VALUES ('Cineplexx Zagreb', 'Slavonska Avenija 11D, Zagreb',
        'https://example.com/zagreb-cineplexx.jpg',
        (SELECT id FROM locations WHERE city = 'Zagreb'))
SELECT 1 FROM dual;

COMMIT;
