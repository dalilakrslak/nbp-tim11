DECLARE
v_nbp_user_id NUMBER(16);
BEGIN
BEGIN
SELECT nbp_user_id
INTO v_nbp_user_id
FROM users
WHERE email = 'user@cinemaapp.com';
EXCEPTION
        WHEN NO_DATA_FOUND THEN
BEGIN
SELECT id
INTO v_nbp_user_id
FROM NBP.NBP_USER
WHERE email = 'user@cinemaapp.com'
   OR username = 'user-cinema-app';
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
                        'userCinemaApp',
                        'UserCinemaApp',
                        'user@cinemaapp.com',
                        '$2a$10$QPx/NTp19.QNiQz/gxVWS.JBr887k050yaMyseUobXvAn4NttAgxm',
                        'user-cinema-app',
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
           'user@cinemaapp.com',
           '$2a$10$AB67e1In8nZ9C3Lr44E8.uyw6n9zRBASGiI5XZUkLzuI/oP2W1DtC',
           'USER',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP,
           v_nbp_user_id
       );
END;
END;
