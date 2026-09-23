CREATE TABLE exchange_rates (
    id UUID PRIMARY KEY,
    base_currency CHAR(3) NOT NULL,
    quote_currency CHAR(3) NOT NULL,
    rate NUMERIC(19,8) NOT NULL CHECK (rate > 0),
    effective_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_exchange_rate_lookup ON exchange_rates(base_currency, quote_currency, effective_at DESC);

CREATE TABLE settlements (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(80) NOT NULL UNIQUE,
    assignor_id UUID NOT NULL,
    receivable_type VARCHAR(30) NOT NULL,
    face_value NUMERIC(19,4) NOT NULL CHECK (face_value > 0),
    asset_currency CHAR(3) NOT NULL,
    settlement_currency CHAR(3) NOT NULL,
    due_date DATE NOT NULL,
    base_rate_monthly NUMERIC(12,8) NOT NULL,
    spread_monthly NUMERIC(12,8) NOT NULL,
    exchange_rate NUMERIC(19,8),
    net_amount NUMERIC(19,4) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_settlement_statement ON settlements(created_at DESC, assignor_id, settlement_currency);
