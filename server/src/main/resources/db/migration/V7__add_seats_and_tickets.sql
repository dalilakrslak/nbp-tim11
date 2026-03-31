CREATE TABLE seats (
                       id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                       hall_id RAW(16) NOT NULL,
                       seat_code VARCHAR2(20) NOT NULL,
                       seat_type VARCHAR2(20) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_seats_hall FOREIGN KEY (hall_id) REFERENCES halls(id) ON DELETE CASCADE
);

CREATE TABLE tickets (
                         id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                         user_id RAW(16) NOT NULL,
                         screening_id RAW(16) NOT NULL,
                         booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         price NUMBER(10,2) NOT NULL,
                         status VARCHAR2(50) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_tickets_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                         CONSTRAINT fk_tickets_screening FOREIGN KEY (screening_id) REFERENCES screenings(id) ON DELETE CASCADE
);

CREATE TABLE seat_booking (
                              id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                              ticket_id RAW(16) NOT NULL,
                              seat_id RAW(16) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_seat_booking_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
                              CONSTRAINT fk_seat_booking_seat FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE CASCADE,
                              CONSTRAINT unique_ticket_seat UNIQUE (ticket_id, seat_id)
);

-- Row A
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A1', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A2', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A3', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A4', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A5', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A6', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A7', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'A8', 'REGULAR' FROM halls;

-- Row B
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B1', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B2', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B3', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B4', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B5', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B6', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B7', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'B8', 'REGULAR' FROM halls;

-- Row C
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C1', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C2', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C3', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C4', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C5', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C6', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C7', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'C8', 'REGULAR' FROM halls;

-- Row D
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D1', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D2', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D3', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D4', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D5', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D6', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D7', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'D8', 'REGULAR' FROM halls;

-- Row E
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E1', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E2', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E3', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E4', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E5', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E6', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E7', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'E8', 'REGULAR' FROM halls;

-- Row F
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F1', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F2', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F3', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F4', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F5', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F6', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F7', 'REGULAR' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'F8', 'REGULAR' FROM halls;

-- Row G
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G1', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G2', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G3', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G4', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G5', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G6', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G7', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'G8', 'VIP' FROM halls;

-- Row H
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H1', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H2', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H3', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H4', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H5', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H6', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H7', 'VIP' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'H8', 'VIP' FROM halls;

-- Row I
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'I1', 'LOVE' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'I2', 'LOVE' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'I3', 'LOVE' FROM halls;
INSERT INTO seats (hall_id, seat_code, seat_type) SELECT id, 'I4', 'LOVE' FROM halls;