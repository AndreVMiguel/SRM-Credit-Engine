# Diagrama Entidade-Relacionamento

```mermaid
erDiagram
    ASSIGNORS ||--o{ SETTLEMENTS : owns
    RECEIVABLE_PRODUCTS ||--o{ SETTLEMENTS : classifies
    CURRENCIES ||--o{ SETTLEMENTS : values
    CURRENCIES ||--o{ EXCHANGE_RATES : quotes

    ASSIGNORS {
        uuid id PK
        varchar name
    }
    RECEIVABLE_PRODUCTS {
        varchar type PK
        varchar name
        numeric monthly_spread
        boolean active
    }
    CURRENCIES {
        varchar code PK
        varchar name
        boolean active
    }
    SETTLEMENTS {
        uuid id PK
        varchar idempotency_key UK
        uuid assignor_id FK
        varchar receivable_type FK
        varchar asset_currency FK
        varchar settlement_currency FK
        numeric face_value
        date due_date
        numeric net_amount
        bigint version
    }
    EXCHANGE_RATES {
        uuid id PK
        varchar base_currency FK
        varchar quote_currency FK
        numeric rate
        timestamptz effective_at
    }
```

`settlements` referencia o cedente, o produto do recebível e duas moedas: a moeda do ativo e a moeda de liquidação. `exchange_rates` também possui dois relacionamentos com `currencies`: moeda-base e moeda-cotada.

Os campos `spread_monthly` e `exchange_rate` continuam gravados na liquidação como fotografia das condições utilizadas no cálculo. Assim, alterações futuras no cadastro do produto ou nas cotações não modificam o histórico financeiro.

O Flyway cria e popula as tabelas de referência antes de adicionar as chaves estrangeiras. Dessa forma, instalações que já possuem liquidações ou cotações são migradas sem perda de dados.
