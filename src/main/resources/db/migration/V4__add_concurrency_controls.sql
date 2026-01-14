-- 1. Add the concurrency and reservation columns
ALTER TABLE seats
ADD COLUMN reserved_until TIMESTAMP,
ADD COLUMN reserved_by BIGINT,
ADD COLUMN version INTEGER DEFAULT 0;

-- 2. Add an index for Optimization
-- This makes finding expired reservations instant: SELECT * FROM seats WHERE reserved_until < NOW()
CREATE INDEX idx_seats_reserved_until ON seats(reserved_until);