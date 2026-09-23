# SRM Credit Engine

Plataforma full stack para precificação e liquidação multimoedas de recebíveis.

```text
srm-credit-engine/
├── backend/       # Java 21, Spring Boot, Maven, PostgreSQL e Flyway
├── frontend/      # React, TypeScript, Vite, React Query e Nginx
└── docker-compose.yml
```

## Executar com Docker

Pré-requisito: Docker com Docker Compose.

```bash
docker compose up --build
```

- Frontend: `http://localhost:3000`
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Para encerrar: `docker compose down`. Para também remover os dados locais: `docker compose down -v`.

## Executar localmente

Suba somente o PostgreSQL com `docker compose up postgres -d` e execute:

```bash
cd backend && mvn spring-boot:run
```

Para desenvolver o frontend: `cd frontend && npm install && npm run dev`. O Vite encaminha `/api` ao backend local. Em produção, o Nginx faz esse encaminhamento internamente e evita dependência de CORS.

## Frontend

- Simulação automática com debounce e apresentação do valor líquido em tempo real.
- Confirmação de liquidação com chave de idempotência.
- Extrato com filtros e paginação server-side.
- Estados de carregamento, erro e sucesso, além de layout responsivo.
- `components/` contém a apresentação, `api/` a integração HTTP, `hooks/` o comportamento reutilizável e `types/` os contratos.
- React Query gerencia cache, requisições assíncronas e invalidação do extrato.

## Endpoints

| Método | Rota | Função |
|---|---|---|
| POST | `/api/v1/exchange-rates` | Registra uma taxa de câmbio |
| POST | `/api/v1/pricing/simulations` | Simula o valor presente/líquido |
| POST | `/api/v1/settlements` | Liquida um recebível de forma idempotente |
| GET | `/api/v1/settlements` | Extrato paginado com filtros |

Exemplo de taxa BRL/USD:

```json
{
  "baseCurrency": "BRL",
  "quoteCurrency": "USD",
  "rate": 0.20,
  "effectiveAt": "2026-09-20T00:00:00Z"
}
```

Exemplo de simulação:

```json
{
  "faceValue": 10000.00,
  "dueDate": "2026-12-20",
  "receivableType": "MERCANTILE_DUPLICATE",
  "assetCurrency": "BRL",
  "settlementCurrency": "USD",
  "baseRateMonthly": 0.01
}
```

Exemplo de liquidação (o mesmo `idempotencyKey` retorna a liquidação já criada):

```json
{
  "idempotencyKey": "operation-2026-0001",
  "assignorId": "b0b9e4c6-4b87-4fd1-82d4-0fccd5603312",
  "pricing": {
    "faceValue": 10000.00,
    "dueDate": "2026-12-20",
    "receivableType": "MERCANTILE_DUPLICATE",
    "assetCurrency": "BRL",
    "settlementCurrency": "USD",
    "baseRateMonthly": 0.01
  }
}
```

Extrato: `GET /api/v1/settlements?from=2026-09-01T00:00:00Z&to=2026-10-01T00:00:00Z&currency=USD&page=0&size=20`.

## Arquitetura

- `controller`: contrato HTTP e validação dos DTOs.
- `service`: casos de uso, transações e regras de aplicação.
- `strategy`: spread específico de cada recebível.
- `repository`: persistência e consultas.
- `entity`: modelo relacional.
- `dto`: contratos de entrada e saída; entidades não são expostas.
- `exception`: erros de API padronizados.

Valores monetários usam `BigDecimal` e `NUMERIC`. A liquidação é ACID, possui chave única de idempotência e `@Version` para controle otimista. O schema é versionado pelo Flyway; o Hibernate apenas valida a estrutura.

## Modelagem de dados

O modelo normalizado separa os cadastros de `assignors`, `currencies` e `receivable_products` dos fatos financeiros `settlements` e `exchange_rates`. As chaves estrangeiras garantem que não sejam persistidas liquidações com cedente, produto ou moeda inexistente.

Consulte o [diagrama ER](docs/er-diagram.md) para ver entidades, cardinalidades e chaves. A migração `V3__normalize_reference_data.sql` preserva os registros existentes, cria os cadastros correspondentes e só então adiciona as restrições referenciais.

## Fórmula

`VP = Valor de face / (1 + taxa base mensal + spread mensal) ^ (dias / 30)`

Spreads atuais: duplicata mercantil = 1,5% a.m.; cheque pré-datado = 2,5% a.m. A conversão cambial é aplicada ao valor presente.

## Testes

```bash
cd backend && mvn test
```

Os testes iniciais verificam o spread por estratégia e a aplicação da conversão cambial ao final do cálculo.
