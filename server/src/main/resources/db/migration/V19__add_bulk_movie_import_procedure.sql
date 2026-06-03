CREATE OR REPLACE PROCEDURE BULK_CREATE_MOVIES_FROM_XML (
    p_movies_xml IN CLOB,
    p_imported_count OUT NUMBER
) AS
    v_missing_required_fields NUMBER := 0;
    v_invalid_ratings NUMBER := 0;
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

    INSERT INTO movies (
        title,
        synopsis,
        duration,
        start_date,
        end_date,
        pg_rating,
        language,
        trailer_url,
        director
    )
    SELECT
        TRIM(x.title),
        NULLIF(TRIM(x.synopsis), ''),
        x.duration,
        COALESCE(
            TO_TIMESTAMP(NULLIF(TRIM(x.start_date), ''), 'YYYY-MM-DD'),
            CURRENT_TIMESTAMP
        ),
        COALESCE(
            TO_TIMESTAMP(NULLIF(TRIM(x.end_date), ''), 'YYYY-MM-DD'),
            COALESCE(
                TO_TIMESTAMP(NULLIF(TRIM(x.start_date), ''), 'YYYY-MM-DD'),
                CURRENT_TIMESTAMP
            ) + NUMTODSINTERVAL(30, 'DAY')
        ),
        CASE UPPER(REPLACE(TRIM(x.pg_rating), '-', '_'))
            WHEN 'G' THEN 'G'
            WHEN 'PG' THEN 'PG'
            WHEN 'PG_13' THEN 'PG_13'
            WHEN 'R' THEN 'R'
        END,
        NULLIF(TRIM(x.language), ''),
        TRIM(x.trailer_url),
        TRIM(x.director)
    FROM XMLTABLE('/movies/movie'
        PASSING XMLTYPE(p_movies_xml)
        COLUMNS
            title VARCHAR2(255) PATH 'title',
            synopsis VARCHAR2(4000) PATH 'synopsis',
            duration NUMBER PATH 'duration',
            start_date VARCHAR2(32) PATH 'startDate',
            end_date VARCHAR2(32) PATH 'endDate',
            pg_rating VARCHAR2(50) PATH 'pgRating',
            language VARCHAR2(255) PATH 'language',
            trailer_url VARCHAR2(1000) PATH 'trailerUrl',
            director VARCHAR2(255) PATH 'director'
    ) x;

    p_imported_count := SQL%ROWCOUNT;
END;
/
