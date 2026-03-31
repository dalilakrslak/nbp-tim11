CREATE TABLE reset_token (
                             id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                             token VARCHAR2(255) NOT NULL,
                             expires_at TIMESTAMP NOT NULL,
                             user_id RAW(16) NOT NULL,
                             CONSTRAINT fk_reset_token_user FOREIGN KEY (user_id)
                                 REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_reset_token_token ON reset_token(token);