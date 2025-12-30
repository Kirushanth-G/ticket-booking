ALTER TABLE bookings
ADD CONSTRAINT uk_user_ticket UNIQUE (user_id, ticket_type_id);
