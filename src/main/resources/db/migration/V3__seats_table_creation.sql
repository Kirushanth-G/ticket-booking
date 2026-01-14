CREATE TYPE seat_status AS ENUM ('AVAILABLE', 'RESERVED', 'BOOKED', 'BLOCKED');
CREATE TABLE seats (
    seat_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_type_id BIGINT NOT NULL,
    seat_no VARCHAR(10) NOT NULL,    -- "A1"
    status seat_status DEFAULT 'AVAILABLE', -- AVAILABLE, RESERVED, BOOKED
    FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(ticket_type_id),
    UNIQUE (ticket_type_id, seat_no) -- Cannot have two 'A1' seats in the same VIP section
);

ALTER TABLE ticket_types
DROP COLUMN IF EXISTS quantity,
DROP COLUMN IF EXISTS version;

ALTER TABLE bookings
ADD COLUMN IF NOT EXISTS event_id BIGINT NOT NULL,
ADD CONSTRAINT fk_bookings_event_id FOREIGN KEY (event_id) REFERENCES events(event_id),
ADD COLUMN IF NOT EXISTS total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0;

DO $$
DECLARE r RECORD;
BEGIN
  FOR r IN
    SELECT tc.constraint_name
    FROM information_schema.table_constraints tc
    JOIN information_schema.key_column_usage kcu
      ON tc.constraint_name = kcu.constraint_name
      AND tc.constraint_schema = kcu.constraint_schema
    WHERE tc.table_name = 'bookings'
      AND kcu.column_name = 'ticket_type_id'
      AND tc.constraint_type = 'FOREIGN KEY'
  LOOP
    EXECUTE format('ALTER TABLE bookings DROP CONSTRAINT IF EXISTS %I', r.constraint_name);
  END LOOP;
END$$;

ALTER TABLE bookings
DROP CONSTRAINT IF EXISTS uk_user_ticket,
DROP COLUMN IF EXISTS seats_booked,
DROP COLUMN IF EXISTS ticket_type_id;


CREATE TABLE booking_items (
    booking_item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    booking_id BIGINT NOT NULL,     -- The Receipt
    seat_id BIGINT NOT NULL,        -- The Specific Chair (e.g., Seat A1)

    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
    FOREIGN KEY (seat_id) REFERENCES seats(seat_id)
);