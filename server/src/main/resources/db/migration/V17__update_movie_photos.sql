DELETE FROM movie_photos;

INSERT ALL
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Avatar/avatar.jpg', 1, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Avatar/avatar2.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Mickey17/mickey-17-k5.jpg', 1, (SELECT id FROM movies WHERE title = 'Mickey 17'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Oppenheimer/oppeneimer-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Interstellar/interstellar-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Interstellar'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/POTCBlackPearl/blackpearl-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/OnceUpon/onceupon-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Minecraft/minecraft-cover.jpg', 1, (SELECT id FROM movies WHERE title IN ('Minecraft', 'A Minecraft Movie')))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/CaptainAmericaBraveNewWorld/captainamerica-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/TheUnion/theunion-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'The Union'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/GladiatorII/gladiator2-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Gladiator II'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/BlinkTwice/blinktwice-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Blink Twice'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Wicked/wicked-cover.jpg', 1, (SELECT id FROM movies WHERE title = 'Wicked'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Avatar/4pNlHx6ytdYBDs94PgcS0wQkbc4.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Avatar/rZAUAePCueGzTdDzRQe9wD8x1Ov.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Avatar/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg', 0, (SELECT id FROM movies WHERE title = 'Avatar: The Way of Water'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Mickey17/2S3hRv6KqoZ3vReqlTVk1aHJIU6.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Mickey17/7Oh1xRB8QbMduhqXEUHKlnwxMJi.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Mickey17/cyDndCTSLg6GGVvDQLsaV4fXJzC.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Mickey17/qUc0Hol3eP74dbW4YyqT6oRLYgT.jpg', 0, (SELECT id FROM movies WHERE title = 'Mickey 17'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Oppenheimer/8szKvTWhqnatqrHWloFyyPX1WZc.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Oppenheimer/h0TuquPlfxqe4sJSy7sUlEzaAsL.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Oppenheimer/kMa1TSDj76zTSleXE7xsuZ4s3i0.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Oppenheimer/ycnO0cjsAROSGJKuMODgRtWsHQw.jpg', 0, (SELECT id FROM movies WHERE title = 'Oppenheimer'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Interstellar/5C3RriLKkIAQtQMx85JLtu4rVI2.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Interstellar/l33oR0mnvf20avWyIMxW02EtQxn.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Interstellar/l38yk8r3RLzLYgFFvRYcOiDbvcq.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Interstellar/ln2Gre4IYRhpjuGVybbtaF4CLo5.jpg', 0, (SELECT id FROM movies WHERE title = 'Interstellar'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/POTCBlackPearl/16FT0cxxBK9qQSvZf7F8i0ITQPm.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/POTCBlackPearl/dm0Q0RtV6U8pgM0mXvsNJpwRayP.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/POTCBlackPearl/nr6SuKESwEPZnExvxSzQczF1iBt.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/POTCBlackPearl/tDNHCZpTaLhVF7awv1PYgDkvHJU.jpg', 0, (SELECT id FROM movies WHERE title = 'Pirates of the Caribbean: The Curse of the Black Pearl'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/OnceUpon/ltUKAxoQ4GRu7EaUNg8GxD9vZ6u.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/OnceUpon/nmsutFaFnlY1N85mPDt0r4fWynL.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/OnceUpon/oRiUKwDpcqDdoLwPoA4FIRh3hqY.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/OnceUpon/vm8C7lAob4hSn8MvHGa9RBLy7rR.jpg', 0, (SELECT id FROM movies WHERE title = 'Once Upon a Time... in Hollywood'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Minecraft/4MWc9Ur80Wo0B1fVVTnV0CoSh6A.jpg', 0, (SELECT id FROM movies WHERE title IN ('Minecraft', 'A Minecraft Movie')))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Minecraft/lEkq2xPYtvEGx2iKkClN3p5uxFQ.jpg', 0, (SELECT id FROM movies WHERE title IN ('Minecraft', 'A Minecraft Movie')))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Minecraft/lEkq2xPYtvEGx2iKkClN3p5uxFQ.jpg', 0, (SELECT id FROM movies WHERE title IN ('Minecraft', 'A Minecraft Movie')))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Minecraft/ws2UKX5dtQlK6sJWbVaikCOJbxD.jpg', 0, (SELECT id FROM movies WHERE title IN ('Minecraft', 'A Minecraft Movie')))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/CaptainAmericaBraveNewWorld/4ybBpFNwTQCbBewWrpO2YFNWRuB.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/CaptainAmericaBraveNewWorld/eJLTpgUAFkx165LuUoQqQGyN5Wp.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/CaptainAmericaBraveNewWorld/ncTtBRGnohOCMKfVuu3AfzaL1xE.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/CaptainAmericaBraveNewWorld/qfAfE5auxsuxhxPpnETRAyTP5ff.jpg', 0, (SELECT id FROM movies WHERE title = 'Captain America: Brave New World'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/TheUnion/ADJon1zAvcParV7A1e6q6z0KVQ.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/TheUnion/dqsWbl7aubfUn6OqmkmXPISj2W6.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/TheUnion/mFhihsHSoyIqZbK7IgspWwq23ww.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/TheUnion/vRHOXQhTrlWp0Hzr1b5Qn2Fa3bx.jpg', 0, (SELECT id FROM movies WHERE title = 'The Union'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/GladiatorII/8mjYwWT50GkRrrRdyHzJorfEfcl.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/GladiatorII/A4SDLzUM9RJVdQc3gWOX4epGLM7.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/GladiatorII/bHeUgZKqduubnNl8GshjrpHS9lF.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/GladiatorII/xGv28mcf8vdFttD0KgI5GnDDlkG.jpg', 0, (SELECT id FROM movies WHERE title = 'Gladiator II'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/BlinkTwice/11xVvX1H7alRDfGHztA4lNvocDh.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/BlinkTwice/h6K9vwhbogdqXMxIpFpqKS9bEjQ.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/BlinkTwice/yUJpsoVT51seW9YG3jrRh9JS5S0.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/BlinkTwice/zQuOiqlktwgR64WW07PFWpjxWGs.jpg', 0, (SELECT id FROM movies WHERE title = 'Blink Twice'))

    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Wicked/830sdPfXpyizgm05HR3LR6vFJrL.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Wicked/jTOeWjamUKGxWVUO1TMZXqQUarw.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Wicked/k4xavRmJUzf3M2LRDxhlPq8R6zy.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
    INTO movie_photos (url, is_cover_image, movie_id) VALUES ('https://uzdlatfpubfuksghkzun.supabase.co/storage/v1/object/public/NBP/Wicked/uDjYG4ODYetiNuRaopvLvRq0RuO.jpg', 0, (SELECT id FROM movies WHERE title = 'Wicked'))
SELECT 1 FROM dual;

COMMIT;