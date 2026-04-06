BEGIN
    FOR t IN (
        SELECT table_name
        FROM user_tables
        WHERE table_name NOT IN ('flyway_schema_history')
        ) LOOP
            BEGIN
                -- Drop trigger
                BEGIN
                    EXECUTE IMMEDIATE 'DROP TRIGGER trg_' || t.table_name;
                EXCEPTION
                    WHEN OTHERS THEN NULL;
                END;

                -- Create trigger
                EXECUTE IMMEDIATE '
                CREATE OR REPLACE TRIGGER trg_' || t.table_name || '
                AFTER INSERT OR UPDATE OR DELETE ON "' || t.table_name || '"
                FOR EACH ROW
                DECLARE
                    v_action VARCHAR2(10);
                BEGIN
                    IF INSERTING THEN
                        v_action := ''INSERT'';
                    ELSIF UPDATING THEN
                        v_action := ''UPDATE'';
                    ELSIF DELETING THEN
                        v_action := ''DELETE'';
                    END IF;

                    BEGIN
                        INSERT INTO nbp.nbp_log (
                            table_name,
                            action_name,
                            date_time,
                            db_user
                        )
                        VALUES (
                            ''' || t.table_name || ''',
                            v_action,
                            SYSDATE,
                            SYS_CONTEXT(''USERENV'', ''SESSION_USER'')
                        );
                    EXCEPTION
                        WHEN OTHERS THEN NULL;
                    END;
                END;';
            EXCEPTION
                WHEN OTHERS THEN
                    RAISE;
            END;
        END LOOP;
END;
/
