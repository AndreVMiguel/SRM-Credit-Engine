-- V1 originally used CHAR(3), while the JPA mappings expect VARCHAR(3).
-- This migration upgrades databases that already applied the original V1.
ALTER TABLE exchange_rates
    ALTER COLUMN base_currency TYPE VARCHAR(3),
    ALTER COLUMN quote_currency TYPE VARCHAR(3);

ALTER TABLE settlements
    ALTER COLUMN asset_currency TYPE VARCHAR(3),
    ALTER COLUMN settlement_currency TYPE VARCHAR(3);
