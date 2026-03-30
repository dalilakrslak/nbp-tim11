UPDATE screenings
SET hall_id = (SELECT id FROM halls FETCH FIRST 1 ROWS ONLY)
WHERE hall_id IS NULL;

ALTER TABLE screenings MODIFY hall_id NOT NULL;

-- Update upcoming movie start dates to showcase different badge dates
UPDATE movies SET start_date = CURRENT_DATE + NUMTODSINTERVAL(2, 'DAY') WHERE title = 'Minecraft';
UPDATE movies SET start_date = CURRENT_DATE + NUMTODSINTERVAL(5, 'DAY') WHERE title = 'Captain America: Brave New World';
UPDATE movies SET start_date = ADD_MONTHS(CURRENT_DATE + NUMTODSINTERVAL(3, 'DAY'), 1) WHERE title = 'The Union';
UPDATE movies SET start_date = ADD_MONTHS(CURRENT_DATE + NUMTODSINTERVAL(15, 'DAY'), 1) WHERE title = 'Gladiator II';
UPDATE movies SET start_date = ADD_MONTHS(CURRENT_DATE + NUMTODSINTERVAL(7, 'DAY'), 2) WHERE title = 'Blink Twice';
UPDATE movies SET start_date = ADD_MONTHS(CURRENT_DATE + NUMTODSINTERVAL(25, 'DAY'), 2) WHERE title = 'Wicked';

-- Add genres
INSERT INTO genres (name) VALUES ('Documentary');
INSERT INTO genres (name) VALUES ('Anime');
