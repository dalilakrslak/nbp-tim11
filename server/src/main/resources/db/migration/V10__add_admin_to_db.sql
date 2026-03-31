DECLARE
    v_nbp_user_id NUMBER(16);
BEGIN
    BEGIN
        SELECT nbp_user_id
        INTO v_nbp_user_id
        FROM users
        WHERE email = 'admin@cinemaapp.com';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            BEGIN
                SELECT id
                INTO v_nbp_user_id
                FROM NBP.NBP_USER
                WHERE email = 'admin@cinemaapp.com'
                   OR username = 'admin-cinema-app';
            EXCEPTION
                WHEN NO_DATA_FOUND THEN
                    INSERT INTO NBP.NBP_USER (
                        ID,
                        FIRST_NAME,
                        LAST_NAME,
                        EMAIL,
                        PASSWORD,
                        USERNAME,
                        PHONE_NUMBER,
                        BIRTH_DATE,
                        ADDRESS_ID,
                        ROLE_ID
                    )
                    VALUES (
                        NBP.NBP_USER_ID_SEQ.NEXTVAL,
                        'AdminCinemaApp',
                        'UserCinemaApp',
                        'admin@cinemaapp.com',
                        '$2a$10$QPx/NTp19.QNiQz/gxVWS.JBr887k050yaMyseUobXvAn4NttAgxm',
                        'admin-cinema-app',
                        '000000000',
                        NULL,
                        0,
                        0
                    )
                    RETURNING ID INTO v_nbp_user_id;
            END;

            INSERT INTO users (
                email,
                password,
                role,
                created_at,
                updated_at,
                nbp_user_id
            )
            VALUES (
                'admin@cinemaapp.com',
                '$2a$10$QPx/NTp19.QNiQz/gxVWS.JBr887k050yaMyseUobXvAn4NttAgxm',
                'ADMIN',
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                v_nbp_user_id
            );
    END;
END;
