CREATE TABLE currencies (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO currencies (code, name, active) VALUES
    ('BRL', 'Real brasileiro', TRUE),
    ('USD', 'Dólar americano', TRUE)
ON CONFLICT (code) DO NOTHING;

INSERT INTO currencies (code, name, active)
SELECT currency_code, currency_code, TRUE
FROM (
    SELECT base_currency AS currency_code FROM exchange_rates
    UNION SELECT quote_currency FROM exchange_rates
    UNION SELECT asset_currency FROM settlements
    UNION SELECT settlement_currency FROM settlements
) existing_currencies
WHERE currency_code IS NOT NULL
ON CONFLICT (code) DO NOTHING;

CREATE TABLE assignors (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL
);

INSERT INTO assignors (id, name)
SELECT DISTINCT assignor_id, CONCAT('Cedente ', LEFT(assignor_id::TEXT, 8))
FROM settlements
ON CONFLICT (id) DO NOTHING;

CREATE TABLE receivable_products (
    type VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    monthly_spread NUMERIC(12,8) NOT NULL CHECK (monthly_spread >= 0),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO receivable_products (type, name, monthly_spread, active) VALUES
    ('MERCANTILE_DUPLICATE', 'Duplicata mercantil', 0.01500000, TRUE),
    ('POSTDATED_CHECK', 'Cheque pré-datado', 0.02500000, TRUE)
ON CONFLICT (type) DO NOTHING;

ALTER TABLE exchange_rates
    ADD CONSTRAINT fk_exchange_rate_base_currency
        FOREIGN KEY (base_currency) REFERENCES currencies (code),
    ADD CONSTRAINT fk_exchange_rate_quote_currency
        FOREIGN KEY (quote_currency) REFERENCES currencies (code);

ALTER TABLE settlements
    ADD CONSTRAINT fk_settlement_assignor
        FOREIGN KEY (assignor_id) REFERENCES assignors (id),
    ADD CONSTRAINT fk_settlement_receivable_product
        FOREIGN KEY (receivable_type) REFERENCES receivable_products (type),
    ADD CONSTRAINT fk_settlement_asset_currency
        FOREIGN KEY (asset_currency) REFERENCES currencies (code),
    ADD CONSTRAINT fk_settlement_settlement_currency
        FOREIGN KEY (settlement_currency) REFERENCES currencies (code);

CREATE INDEX idx_settlement_receivable_product ON settlements (receivable_type);
CREATE INDEX idx_settlement_asset_currency ON settlements (asset_currency);
CREATE INDEX idx_exchange_rate_quote_currency ON exchange_rates (quote_currency);
