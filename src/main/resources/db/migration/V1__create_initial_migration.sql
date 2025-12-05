CREATE TYPE status_enum AS ENUM ('UPCOMING', 'ON_SALE', 'SOLD_OUT', 'CANCELLED');

CREATE TABLE events(
    event_id BIGSERIAL PRIMARY KEY,
    event_name VARCHAR(255) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    venue VARCHAR(255),
    status status_enum DEFAULT 'UPCOMING'
);

CREATE TABLE ticket_types(
    ticket_type_id BIGSERIAL PRIMARY KEY,
    event_id BIGINT,
    type_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    version INTEGER DEFAULT 0,

    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

CREATE TABLE bookings(
    booking_id BIGSERIAL PRIMARY KEY,
    ticket_type_id BIGINT,
    user_id BIGINT NOT NULL,
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    seats_booked INT NOT NULL,

    FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(ticket_type_id)
);