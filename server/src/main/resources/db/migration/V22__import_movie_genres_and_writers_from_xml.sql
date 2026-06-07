CREATE OR REPLACE PROCEDURE BULK_CREATE_MOVIES_FROM_XML (
    p_movies_xml IN CLOB,
    p_imported_count OUT NUMBER
) AS
    v_missing_required_fields NUMBER := 0;
    v_invalid_ratings NUMBER := 0;
    v_missing_photo_urls NUMBER := 0;
    v_invalid_photo_cover_flags NUMBER := 0;
    v_missing_movie_genres NUMBER := 0;
    v_missing_genre_names NUMBER := 0;
    v_missing_movie_writers NUMBER := 0;
    v_missing_writer_names NUMBER := 0;
    v_movie_id RAW(16);
    v_genre_id RAW(16);
    v_writer_id RAW(16);
BEGIN
    SELECT COUNT(*)
    INTO v_missing_required_fields
    FROM XMLTABLE('/movies/movie'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            title VARCHAR2(255) PATH 'title',
            duration NUMBER PATH 'duration',
            pg_rating VARCHAR2(50) PATH 'pgRating',
            language VARCHAR2(255) PATH 'language',
            trailer_url VARCHAR2(1000) PATH 'trailerUrl',
            director VARCHAR2(255) PATH 'director'
    ) x
    WHERE TRIM(x.title) IS NULL
       OR x.duration IS NULL
       OR TRIM(x.pg_rating) IS NULL
       OR TRIM(x.language) IS NULL
       OR TRIM(x.trailer_url) IS NULL
       OR TRIM(x.director) IS NULL;

    IF v_missing_required_fields > 0 THEN
        raise_application_error(
            -20001,
            'Each movie must include title, duration, pgRating, language, trailerUrl and director.'
        );
    END IF;

    SELECT COUNT(*)
    INTO v_invalid_ratings
    FROM XMLTABLE('/movies/movie'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            pg_rating VARCHAR2(50) PATH 'pgRating'
    ) x
    WHERE x.pg_rating IS NOT NULL
      AND UPPER(REPLACE(TRIM(x.pg_rating), '-', '_')) NOT IN ('G', 'PG', 'PG_13', 'R');

    IF v_invalid_ratings > 0 THEN
        raise_application_error(
            -20002,
            'Invalid pgRating value found in XML. Allowed values are G, PG, PG-13, PG_13 and R.'
        );
    END IF;

    SELECT COUNT(*)
    INTO v_missing_photo_urls
    FROM XMLTABLE('/movies/movie/photos/photo'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            url VARCHAR2(4000) PATH 'url',
            attr_url VARCHAR2(4000) PATH '@url'
    ) x
    WHERE TRIM(COALESCE(x.url, x.attr_url)) IS NULL;

    IF v_missing_photo_urls > 0 THEN
        raise_application_error(
            -20003,
            'Each movie photo must include a url.'
        );
    END IF;

    SELECT COUNT(*)
    INTO v_invalid_photo_cover_flags
    FROM XMLTABLE('/movies/movie/photos/photo'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            is_cover_image VARCHAR2(20) PATH 'isCoverImage',
            cover_image VARCHAR2(20) PATH 'coverImage',
            is_cover_image_snake VARCHAR2(20) PATH 'is_cover_image',
            attr_is_cover_image VARCHAR2(20) PATH '@isCoverImage',
            attr_cover_image VARCHAR2(20) PATH '@coverImage',
            attr_is_cover_image_snake VARCHAR2(20) PATH '@is_cover_image'
    ) x
    WHERE TRIM(COALESCE(
              x.is_cover_image,
              x.cover_image,
              x.is_cover_image_snake,
              x.attr_is_cover_image,
              x.attr_cover_image,
              x.attr_is_cover_image_snake
          )) IS NOT NULL
      AND UPPER(TRIM(COALESCE(
              x.is_cover_image,
              x.cover_image,
              x.is_cover_image_snake,
              x.attr_is_cover_image,
              x.attr_cover_image,
              x.attr_is_cover_image_snake
          ))) NOT IN ('TRUE', 'FALSE', '1', '0', 'YES', 'NO', 'Y', 'N');

    IF v_invalid_photo_cover_flags > 0 THEN
        raise_application_error(
            -20004,
            'Invalid movie photo cover flag found in XML. Allowed values are true, false, 1, 0, yes, no, y and n.'
        );
    END IF;

    SELECT COUNT(*)
    INTO v_missing_movie_genres
    FROM XMLTABLE('/movies/movie'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            genre_count NUMBER PATH 'count(genres/genre)'
    ) x
    WHERE x.genre_count = 0;

    IF v_missing_movie_genres > 0 THEN
        raise_application_error(
            -20005,
            'Each movie must include at least one genre.'
        );
    END IF;

    SELECT COUNT(*)
    INTO v_missing_genre_names
    FROM XMLTABLE('/movies/movie/genres/genre'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            name VARCHAR2(255) PATH 'name',
            attr_name VARCHAR2(255) PATH '@name',
            text_value VARCHAR2(255) PATH 'text()'
    ) x
    WHERE TRIM(COALESCE(x.name, x.attr_name, x.text_value)) IS NULL;

    IF v_missing_genre_names > 0 THEN
        raise_application_error(
            -20006,
            'Each movie genre must include a name.'
        );
    END IF;

    SELECT COUNT(*)
    INTO v_missing_movie_writers
    FROM XMLTABLE('/movies/movie'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            writer_count NUMBER PATH 'count(writers/writer)'
    ) x
    WHERE x.writer_count = 0;

    IF v_missing_movie_writers > 0 THEN
        raise_application_error(
            -20007,
            'Each movie must include at least one writer.'
        );
    END IF;

    SELECT COUNT(*)
    INTO v_missing_writer_names
    FROM XMLTABLE('/movies/movie/writers/writer'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            first_name VARCHAR2(255) PATH 'firstName',
            first_name_snake VARCHAR2(255) PATH 'first_name',
            attr_first_name VARCHAR2(255) PATH '@firstName',
            attr_first_name_snake VARCHAR2(255) PATH '@first_name',
            last_name VARCHAR2(255) PATH 'lastName',
            last_name_snake VARCHAR2(255) PATH 'last_name',
            attr_last_name VARCHAR2(255) PATH '@lastName',
            attr_last_name_snake VARCHAR2(255) PATH '@last_name',
            full_name VARCHAR2(511) PATH 'fullName',
            attr_full_name VARCHAR2(511) PATH '@fullName',
            text_value VARCHAR2(511) PATH 'text()'
    ) x
    WHERE TRIM(COALESCE(
              x.first_name,
              x.first_name_snake,
              x.attr_first_name,
              x.attr_first_name_snake,
              REGEXP_SUBSTR(TRIM(COALESCE(x.full_name, x.attr_full_name, x.text_value)), '^[^[:space:]]+')
          )) IS NULL
       OR TRIM(COALESCE(
              x.last_name,
              x.last_name_snake,
              x.attr_last_name,
              x.attr_last_name_snake,
              CASE
                  WHEN INSTR(TRIM(COALESCE(x.full_name, x.attr_full_name, x.text_value)), ' ') > 0
                  THEN REGEXP_REPLACE(TRIM(COALESCE(x.full_name, x.attr_full_name, x.text_value)), '^[^[:space:]]+[[:space:]]*')
              END
          )) IS NULL;

    IF v_missing_writer_names > 0 THEN
        raise_application_error(
            -20008,
            'Each movie writer must include firstName and lastName, or a fullName containing both names.'
        );
    END IF;

    p_imported_count := 0;

    FOR movie_record IN (
        SELECT
            x.movie_xml,
            x.title,
            x.synopsis,
            x.duration,
            x.start_date,
            x.end_date,
            x.pg_rating,
            x.language,
            x.trailer_url,
            x.director
        FROM XMLTABLE('/movies/movie'
            PASSING XMLTYPE(p_movies_xml)
            COLUMNS
                movie_xml XMLTYPE PATH '.',
                title VARCHAR2(255) PATH 'title',
                synopsis VARCHAR2(4000) PATH 'synopsis',
                duration NUMBER PATH 'duration',
                start_date VARCHAR2(32) PATH 'startDate',
                end_date VARCHAR2(32) PATH 'endDate',
                pg_rating VARCHAR2(50) PATH 'pgRating',
                language VARCHAR2(255) PATH 'language',
                trailer_url VARCHAR2(1000) PATH 'trailerUrl',
                director VARCHAR2(255) PATH 'director'
        ) x
    ) LOOP
        v_movie_id := SYS_GUID();

        INSERT INTO movies (
            id,
            title,
            synopsis,
            duration,
            start_date,
            end_date,
            pg_rating,
            language,
            trailer_url,
            director
        ) VALUES (
            v_movie_id,
            TRIM(movie_record.title),
            NULLIF(TRIM(movie_record.synopsis), ''),
            movie_record.duration,
            COALESCE(
                TO_TIMESTAMP(NULLIF(TRIM(movie_record.start_date), ''), 'YYYY-MM-DD'),
                CURRENT_TIMESTAMP
            ),
            COALESCE(
                TO_TIMESTAMP(NULLIF(TRIM(movie_record.end_date), ''), 'YYYY-MM-DD'),
                COALESCE(
                    TO_TIMESTAMP(NULLIF(TRIM(movie_record.start_date), ''), 'YYYY-MM-DD'),
                    CURRENT_TIMESTAMP
                ) + NUMTODSINTERVAL(30, 'DAY')
            ),
            CASE UPPER(REPLACE(TRIM(movie_record.pg_rating), '-', '_'))
                WHEN 'G' THEN 'G'
                WHEN 'PG' THEN 'PG'
                WHEN 'PG_13' THEN 'PG_13'
                WHEN 'R' THEN 'R'
            END,
            NULLIF(TRIM(movie_record.language), ''),
            TRIM(movie_record.trailer_url),
            TRIM(movie_record.director)
        );

        FOR genre_record IN (
            SELECT DISTINCT
                TRIM(COALESCE(g.name, g.attr_name, g.text_value)) AS name
            FROM XMLTABLE('/movie/genres/genre'
                PASSING movie_record.movie_xml
                COLUMNS
                    name VARCHAR2(255) PATH 'name',
                    attr_name VARCHAR2(255) PATH '@name',
                    text_value VARCHAR2(255) PATH 'text()'
            ) g
        ) LOOP
            SELECT MIN(id)
            INTO v_genre_id
            FROM genres existing_genre
            WHERE LOWER(TRIM(existing_genre.name)) = LOWER(genre_record.name);

            IF v_genre_id IS NULL THEN
                v_genre_id := SYS_GUID();

                INSERT INTO genres (
                    id,
                    name
                ) VALUES (
                    v_genre_id,
                    genre_record.name
                );
            END IF;

            INSERT INTO movie_genre (
                movie_id,
                genre_id
            )
            SELECT
                v_movie_id,
                v_genre_id
            FROM dual
            WHERE NOT EXISTS (
                SELECT 1
                FROM movie_genre
                WHERE movie_id = v_movie_id
                  AND genre_id = v_genre_id
            );
        END LOOP;

        FOR writer_record IN (
            SELECT DISTINCT
                TRIM(COALESCE(
                    w.first_name,
                    w.first_name_snake,
                    w.attr_first_name,
                    w.attr_first_name_snake,
                    REGEXP_SUBSTR(TRIM(COALESCE(w.full_name, w.attr_full_name, w.text_value)), '^[^[:space:]]+')
                )) AS first_name,
                TRIM(COALESCE(
                    w.last_name,
                    w.last_name_snake,
                    w.attr_last_name,
                    w.attr_last_name_snake,
                    CASE
                        WHEN INSTR(TRIM(COALESCE(w.full_name, w.attr_full_name, w.text_value)), ' ') > 0
                        THEN REGEXP_REPLACE(TRIM(COALESCE(w.full_name, w.attr_full_name, w.text_value)), '^[^[:space:]]+[[:space:]]*')
                    END
                )) AS last_name
            FROM XMLTABLE('/movie/writers/writer'
                PASSING movie_record.movie_xml
                COLUMNS
                    first_name VARCHAR2(255) PATH 'firstName',
                    first_name_snake VARCHAR2(255) PATH 'first_name',
                    attr_first_name VARCHAR2(255) PATH '@firstName',
                    attr_first_name_snake VARCHAR2(255) PATH '@first_name',
                    last_name VARCHAR2(255) PATH 'lastName',
                    last_name_snake VARCHAR2(255) PATH 'last_name',
                    attr_last_name VARCHAR2(255) PATH '@lastName',
                    attr_last_name_snake VARCHAR2(255) PATH '@last_name',
                    full_name VARCHAR2(511) PATH 'fullName',
                    attr_full_name VARCHAR2(511) PATH '@fullName',
                    text_value VARCHAR2(511) PATH 'text()'
            ) w
        ) LOOP
            SELECT MIN(id)
            INTO v_writer_id
            FROM writers existing_writer
            WHERE LOWER(TRIM(existing_writer.first_name)) = LOWER(writer_record.first_name)
              AND LOWER(TRIM(existing_writer.last_name)) = LOWER(writer_record.last_name);

            IF v_writer_id IS NULL THEN
                v_writer_id := SYS_GUID();

                INSERT INTO writers (
                    id,
                    first_name,
                    last_name
                ) VALUES (
                    v_writer_id,
                    writer_record.first_name,
                    writer_record.last_name
                );
            END IF;

            INSERT INTO movie_writer (
                movie_id,
                writer_id
            )
            SELECT
                v_movie_id,
                v_writer_id
            FROM dual
            WHERE NOT EXISTS (
                SELECT 1
                FROM movie_writer
                WHERE movie_id = v_movie_id
                  AND writer_id = v_writer_id
            );
        END LOOP;

        FOR photo_record IN (
            SELECT
                TRIM(COALESCE(p.url, p.attr_url)) AS url,
                UPPER(TRIM(COALESCE(
                    p.is_cover_image,
                    p.cover_image,
                    p.is_cover_image_snake,
                    p.attr_is_cover_image,
                    p.attr_cover_image,
                    p.attr_is_cover_image_snake
                ))) AS cover_flag
            FROM XMLTABLE('/movie/photos/photo'
                PASSING movie_record.movie_xml
                COLUMNS
                    url VARCHAR2(4000) PATH 'url',
                    attr_url VARCHAR2(4000) PATH '@url',
                    is_cover_image VARCHAR2(20) PATH 'isCoverImage',
                    cover_image VARCHAR2(20) PATH 'coverImage',
                    is_cover_image_snake VARCHAR2(20) PATH 'is_cover_image',
                    attr_is_cover_image VARCHAR2(20) PATH '@isCoverImage',
                    attr_cover_image VARCHAR2(20) PATH '@coverImage',
                    attr_is_cover_image_snake VARCHAR2(20) PATH '@is_cover_image'
            ) p
        ) LOOP
            INSERT INTO movie_photos (
                url,
                is_cover_image,
                movie_id
            ) VALUES (
                photo_record.url,
                CASE
                    WHEN photo_record.cover_flag IN ('TRUE', '1', 'YES', 'Y') THEN 1
                    ELSE 0
                END,
                v_movie_id
            );
        END LOOP;

        p_imported_count := p_imported_count + 1;
    END LOOP;
END;
/
