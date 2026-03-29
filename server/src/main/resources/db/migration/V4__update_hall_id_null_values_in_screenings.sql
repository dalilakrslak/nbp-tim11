BEGIN;

UPDATE screenings
SET hall_id = (SELECT id FROM halls LIMIT 1)
WHERE hall_id IS NULL;

ALTER TABLE screenings ALTER COLUMN hall_id SET NOT NULL;

-- Update upcoming movie start dates to showcase different badge dates
UPDATE movies SET start_date = CURRENT_DATE + INTERVAL '2 days' WHERE title = 'Minecraft';
UPDATE movies SET start_date = CURRENT_DATE + INTERVAL '5 days' WHERE title = 'Captain America: Brave New World';
UPDATE movies SET start_date = CURRENT_DATE + INTERVAL '1 month 3 days' WHERE title = 'The Union';
UPDATE movies SET start_date = CURRENT_DATE + INTERVAL '1 month 15 days' WHERE title = 'Gladiator II';
UPDATE movies SET start_date = CURRENT_DATE + INTERVAL '2 months 7 days' WHERE title = 'Blink Twice';
UPDATE movies SET start_date = CURRENT_DATE + INTERVAL '2 months 25 days' WHERE title = 'Wicked';

-- Add genres
INSERT INTO genres (name) VALUES ('Documentary'), ('Anime');

COMMIT;
